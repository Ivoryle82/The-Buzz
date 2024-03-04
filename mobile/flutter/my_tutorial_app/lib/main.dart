import 'package:flutter/material.dart';
import 'package:english_words/english_words.dart';
import 'dart:developer' as developer; //required for developer.log to work

//import files:
import './models/NumberWordPair.dart';
import './net/webRequests.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // TRY THIS: Try running your application with "flutter run". You'll see
        // the application has a purple toolbar. Then, without quitting the app,
        // try changing the seedColor in the colorScheme below to Colors.green
        // and then invoke "hot reload" (save your changes or press the "hot
        // reload" button in a Flutter-supported IDE, or press "r" if you used
        // the command line to start the app).
        //
        // Notice that the counter didn't reset back to zero; the application
        // state is not lost during the reload. To reset the state, use hot
        // restart instead.
        //
        // This works for code too, not just values: Most code changes can be
        // tested with just a hot reload.
        colorScheme: ColorScheme.fromSeed(seedColor: Color.fromARGB(255, 63, 174, 55)),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // TRY THIS: Try changing the color here to a specific color (to
        // Colors.amber, perhaps?) and trigger a hot reload to see the AppBar
        // change color while the other colors stay the same.
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: HttpReqWords(),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}

class HttpReqWords extends StatefulWidget {
  const HttpReqWords({super.key});

  @override
  State<HttpReqWords> createState() => _HttpReqWordsState();
}

class _HttpReqWordsState extends State<HttpReqWords> {
  final _words = <String>['a', 'b', 'c', 'd'];
  final _biggerFont = const TextStyle(fontSize: 18);
  late Future<List<String>> _future_words;
  late Future<List<NumberWordPair>> _future_list_numword_pairs;

  @override
  void initState() {
    super.initState();
    _future_words = doSomeLongRunningCalculation();
    _future_list_numword_pairs = fetchNumberWordPairs();
  }

  void _retry() {
    setState(() {
      _future_list_numword_pairs = fetchNumberWordPairs();
    });
  }

  @override
  Widget build(BuildContext context) {
    //This will get more interesting as we progress in the tutorial
    //for now, simply return a Text widget holding a random word pair
    // final wordPair = WordPair.random();
    // return Text(wordPair.asPascalCase);
    return build_v3(context);
  }

  Widget build_v3(BuildContext context) {
    var fb = FutureBuilder<List<NumberWordPair>>(
      future: _future_list_numword_pairs,
      builder: (BuildContext context, AsyncSnapshot<List<NumberWordPair>> snapshot) {
        Widget child;

        if(snapshot.hasData) {
          //developer.log('`using` ${snapshot.data}', name: 'my.app.category');
          //create listview to showone row per array element of json response
          child = ListView.builder(
            //shrinkWrap: true, //expensive! conside refactoring https://api.flutter.dev/flutter/widgets/ScrollView/shrinkWrap.html
            padding: const EdgeInsets.all(16.0),
            itemCount: snapshot.data!.length,
            itemBuilder: /*1*/ (context, i) {
              return Column(
                children: <Widget>[
                  ListTile(
                    title: Text(
                      "row ${i}: num=${snapshot.data![i].num} str=${snapshot.data![i].str}",
                      //snapshot.data![i].str,
                      style: _biggerFont,
                    ),
                  ),
                  Divider(height: 1.0,),
                ],
              );
            },
          );
        } else if(snapshot.hasError) { //newly added
          child = Text('${snapshot.error}');
        } else {
          //awaiting snapshot data, return simple text widget
          //child = Text('Calculating answer...');
          child = const CircularProgressIndicator();
        }
        return child;
      },
    );

    return fb;

    /// we're leaving a lot of "code debris" while we learn with prior versions of the build
    /// it helps us see iterations that worked, but we should clean this up when we're done
  }

  @override
  Widget build_v2(BuildContext context) {
    var fb = FutureBuilder<List<String>>(
      future: _future_words,
      builder: (BuildContext context, AsyncSnapshot<List<String>> snapshot) {
        Widget child;

        if(snapshot.hasData) {
          developer.log('`using` ${snapshot.data}', name: 'my.app.category');
          //create listview to show on row per array element of json reponse
          child = ListView.builder(
            //shrinkWrap: true, //expensive! consider refactoring. https://api.flutter.dev/flutter/widgets/ScrollView/shrinkWrap.html
            padding: const EdgeInsets.all(16.0),
            itemCount: snapshot.data!.length,
            itemBuilder: /*1*/ (context, i) {
              return Column(
                children: <Widget>[
                  ListTile(
                    title: Text(
                      snapshot.data![i],
                      style: _biggerFont,
                    ),
                  ),
                  Divider(height: 1.0),
                ],
              );
            },
          );
        } else {
          //awaiting snapshot data, return simple text widget
          //child = Text('Calculating answer...');
          child = const CircularProgressIndicator();
        }
        return child;
      },
    );

    return fb;
  }

  @override
  Widget build_v1(BuildContext context) {
    return ListView.builder(
      padding: const EdgeInsets.all(16.0),
      itemCount: _words.length,
      itemBuilder: /*1*/ (context, i) {
        developer.log("Building with context and $i", name: "my.app.categor");

        return Column(
          children: <Widget>[
            ListTile(
              title: Text(
                _words[i],
                style: _biggerFont,
              ),
            ),
            Divider(height: 1.0),
          ],
        );
      },
    );
  }

  @override
  Widget build_v0(BuildContext context) {
    // this will get more interesting as we progress in the tutorial
    // for now, simply return a Text widget holding a random word pair
    final wordPair = WordPair.random();
    return Text(wordPair.asPascalCase);
  }
}

//NB: NEVER CREATE A FUTURE INSIDE OF A BUILD METHOD, do it from initState or a click event handler
