import 'package:http/http.dart' as http;
import 'package:english_words/english_words.dart';
import '../models/NumberWordPair.dart';
import 'dart:developer' as developer; //required for developer.log to work
import 'dart:convert';

// var url = Uri.https('example.com', 'whatsit/create');
// var response = await http.post(url, body: {'name': 'doodle', 'color': 'blue'});
// print('Response status: ${response.statusCode}');
// print('Response body: ${response.body}');

// print(await http.read(Uri.https('example.com', 'foobar.txt')));
Future<void> doRequests() async {
  var url = Uri.https('example.com', 'whatsit/create'); //Using https, could also use http
  var response = await http.post( //POST returning Future<Response>
                  url, 
                  body: {'name': 'doodle', 'color': 'blue'},
                  headers: {});
  print('Response status: ${response.statusCode}');
  print('Response body: ${response.body}');

  var url2 = Uri.http('www.cse.lehigh.edu', '~spear/courses.json');//example using http
  var response2 = await http.get( 
                  url2,
                  headers: {});
  print('Response2 status: ${response2.statusCode}');
  print('Response2 body: ${response2.body}');

  print(await http.read(Uri.https('example.com', 'foobar.txt')));
}

void my_async_post_method() async {
  var data = {'title': 'My first post'};
  var resp = await http.post(
    Uri.parse('https://jsonplaceholder.typicode.com/posts'),
    headers: {'Content-Type': 'application/json; charset=UTF-8'},
    body: json.encode(data),
  );
  print(resp.body);
}

Future<List<String>> getWebData() async {
  developer.log('Making web request...');
  //var url = Uri.http('www.cse.lehigh.edu', '`spear/courses.json');
  //var url = Uri.parse('http://www.cse.lehigh.edu/~spear/courses.json');
  var url = Uri.parse('http://www.cse.lehigh.edu/~spear/5k.json');
  //var url = Uri.parse('https://jsonplaceholder.typicode.com/albums/1');
  var headers = {"Accept": "application/json"};

  var response = await http.get(url, headers: headers);

  developer.log('Reponse status: ${response.statusCode}');
  developer.log('Reponse headers: ${response.headers}');
  developer.log('Response body: ${response.body}');

  //print(await http.read(url)); //should be same as response.body
  final List<String> returnData;
  if(response.statusCode == 200) { //http.get method returned 'ok'
    var res = jsonDecode(response.body);
    print('json decode: $res');

    if(res is List) { //if the json response is a List, which i think means a map
        returnData = (res as List<dynamic>).map( (x) => x.toString() ).toList();
    } else if(res is Map) { //i was wrong above
      returnData = <String>[(res as Map<String, dynamic>).toString()]; //dynamic is a variable type
    } else {
      developer.log('ERROR: Unexpected json response type (was not a List or Map).');
      returnData = List.empty();
    } //return [response.body]; //returns body as only element of List 
  } else {
    throw Exception('Failed to retrieve web data (server returned ${response.statusCode})');
  }
  return returnData;//List<String>.from(res);
}


Future<List<NumberWordPair>> fetchNumberWordPairs() async {
  final response = await http.get(Uri.parse('http://www.cse.lehigh.edu/~spear/5k.json'));

  if(response.statusCode == 200) {
    //If the server did return a 200 OK response, then parse the .JSON
    final List<NumberWordPair> returnData;
    var res = jsonDecode(response.body);
    print('json decode: $res');

    if(res is List) {
      returnData = (res as List<dynamic>).map( (x) => NumberWordPair.fromJson(x) ).toList();
    } else if(res is Map) {
      returnData = <NumberWordPair>[NumberWordPair.fromJson(res as Map<String, dynamic>)];
    } else {
      developer.log('ERROR: Unexpected json response type (was not a List or Map).');
      returnData = List.empty();
    }
    return returnData;
  } else {
    //if the server did not return a 200 OK response, throw an exception
    throw Exception('Did not receive success status code from request');
  }
}

//method for trying out a long-running calculation
Future<List<String>> doSomeLongRunningCalculation() async {
  //return simpleLongRunningCalculation();
  return getWebData(); // we'll try this next
}

Future<List<String>> simpleLongRunningCalculation() async {
  await Future.delayed(Duration(seconds: 5)); //wait 5 seconds
  List<String> myList = List.generate(100, 
                        (index) => WordPair.random().asPascalCase,
                        growable: true);
  return myList;
}
