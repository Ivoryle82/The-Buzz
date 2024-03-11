// import 'package:english_words/english_words.dart';
// import 'package:flutter/material.dart';
// import 'package:provider/provider.dart';

// void main() {
//   runApp(MyApp());
// }

// class MyApp extends StatelessWidget { //this app is a widget
//   const MyApp({Key? key});

//   @override
//   Widget build(BuildContext context) {
//     return ChangeNotifierProvider(
//       create: (context) => MyAppState(), //App state is changed here and provided by ChangeNotifierProvider
//       child: MaterialApp(
//         title: 'Namer App',
//         theme: ThemeData(
//           useMaterial3: true,
//           colorScheme: ColorScheme.fromSeed(seedColor: Color.fromARGB(255, 77, 15, 212)),
//         ),
//         home: MyHomePage(),
//       ),
//     );
//   }
// }
// /**
//  * as you add to the app, you want to make more stateful widgets because they can change themselves
//  * contain state that is thiers and rebuild whent hat state is changed
//  */
// class MyAppState extends ChangeNotifier {
//   var current = WordPair.random(); //comes fromt he library EnglishWords
//   var favorites = <WordPair>[];
//   var messages = <String>[];

//   void getNext() {
//     current = WordPair.random(); //assigns current to a new random word pair
//     notifyListeners();           //let listeners know that this change occurred
//   }

//   //var favorites = <WordPair>[]; //list of WordPair's


//   void toggleFavorite() {
//     if (favorites.contains(current)) { //allows to add favorites to the list
//       favorites.remove(current);
//     } else {
//       favorites.add(current);
//     }
//     notifyListeners();
//   }

//   void addMessage(String message) { // Method to add a new message
//     messages.add(message);
//     notifyListeners();
//   }
  
//   void editMessage(String oldMessage, String newMessage) {
//     final index = messages.indexOf(oldMessage);
//     if (index != -1) {
//       messages[index] = newMessage;
//       notifyListeners();
//     }
//   }
// }

// // ...

// class MyHomePage extends StatefulWidget {//cmd . stateful widget
//   @override
//   State<MyHomePage> createState() => _MyHomePageState();
// }

// class _MyHomePageState extends State<MyHomePage> { //no longer a widget, but a state
//   var selectedIndex = 0;

//   @override
//   Widget build(BuildContext context) {
//     Widget page;
//     switch (selectedIndex) {
//       case 0:
//         page = GeneratorPage();
//         break;
//       //For when we add new pages
//       case 1:
//         page = FavoritesPage();
//         break;
//       default: 
//         throw UnimplementedError('no widget for $selectedIndex');
//     }

//     return Scaffold(
//       body: Row(
//         children: [
//           SafeArea(
//             child: NavigationRail(
//               extended: false,
//               destinations: [
//                 NavigationRailDestination(
//                   icon: Icon(Icons.home),
//                   label: Text('Home'),
//                 ),
//                 NavigationRailDestination(
//                   icon: Icon(Icons.favorite),
//                   label: Text('Favorites'),
//                 ),
//               ],
//               selectedIndex: selectedIndex, // must be either 0 or 1 because there are only two destinations
//               onDestinationSelected: (value) {
//                 setState(() {
//                   selectedIndex = value; //says the selected index
//                 });
//               },
//             ),
//           ),
//           Expanded(
//             child: Container(
//               color: Theme.of(context).colorScheme.primaryContainer,
//               child: page, //child is the entire generator page
//             ),
//           ),
//         ],
//       ),
//     );
//   }
// }

// class GeneratorPage extends StatelessWidget {
//   final TextEditingController _messageController = TextEditingController();
//   @override
//   Widget build(BuildContext context) {
//     var appState = context.watch<MyAppState>();
//     var pair = appState.current;

//     IconData icon;
//     if (appState.favorites.contains(pair)) {
//       icon = Icons.favorite;
//     } else {
//       icon = Icons.favorite_border;
//     }

//     return Center(
//       child: Column(
//         mainAxisAlignment: MainAxisAlignment.center, //alligns widgets to the center axis
//         children: [
//           // MyCustomForm(),
//           // SizedBox(height: 10),
//           TextField(
//             controller: _messageController,
//             decoration: InputDecoration(
//               hintText: 'Enter your message',
//               border: OutlineInputBorder(),
//             ),
//           ),
//           SizedBox(height: 10),
//           ElevatedButton(
//             onPressed: () {
//               appState.addMessage(_messageController.text); // Call addMessage method
//               _messageController.clear(); // Clear text field after adding message
//             },
//             child: Text('Save Message'),
//           ),
//           SizedBox(height: 10),
//           BigCard(pair: pair),
//           SizedBox(height: 10),
//           Row(
//             mainAxisSize: MainAxisSize.min,
//             children: [
//               ElevatedButton.icon(
//                 onPressed: () {
//                   appState.toggleFavorite();
//                 },
//                 icon: Icon(icon),
//                 label: Text('Like'),
//               ),
//               SizedBox(width: 10),
//               ElevatedButton(
//                 onPressed: () {
//                   appState.getNext();
//                 },
//                 child: Text('Next'),
//               ),
//             ],
//           ),
//         ],
//       ),
//     );
//   }
// }

// // ...

// class BigCard extends StatelessWidget { //cmd . extract widget
//   const BigCard({
//     Key? key,
//     required this.pair,
//   }) : super(key: key);

//   final WordPair pair;

//   @override
//   Widget build(BuildContext context) {
//     var theme = Theme.of(context); //watches ThemeData. if theme changes, this widget knowns about it
//     var style = theme.textTheme.displayMedium!.copyWith(
//       color: theme.colorScheme.onSecondary, //onPrimary, will choose a color that works good on primary colors(prob white)
//     );


//     return Card( //wraps widget in a "card" in the area size of the padding
//       color: theme.colorScheme.primary,
//       child: Padding( //cmd . wrap Padding
//         padding: const EdgeInsets.all(10), // changes space btw widgets
//         child: Text(pair.asLowerCase, 
//         style: style,
//         semanticsLabel: pair.asPascalCase,
//         ),
//       ),
//     );
//   }
// }


// // ...



// class FavoritesPage extends StatelessWidget {
//   @override
//   Widget build(BuildContext context) {
//     var appState = context.watch<MyAppState>();

//     if (appState.favorites.isEmpty && appState.messages.isEmpty) {
//       return Center(
//         child: Text('No favorites or messages yet.'),
//       );
//     }

//     return ListView(
//       children: [
//         if (appState.favorites.isNotEmpty)
//           Padding(
//             padding: const EdgeInsets.all(20),
//             child: Text('You have ${appState.favorites.length} favorites:'),
//           ),
//         for (var pair in appState.favorites)
//           ListTile(
//             leading: Icon(Icons.favorite),
//             title: Text(pair.asLowerCase),
//           ),
//         if (appState.messages.isNotEmpty)
//           Padding(
//             padding: const EdgeInsets.all(20),
//             child: Text('Your saved messages:'),
//           ),
//         for (var message in appState.messages)
//           ListTile(
//             leading: Icon(Icons.message),
//             title: Text(message),
//             trailing: IconButton(
//               icon: Icon(Icons.edit),
//               onPressed: () {
//                 _editMessage(context, appState, message);
//               },
//             ),
//           ),
//       ],
//     );
//   }

//   void _editMessage(BuildContext context, MyAppState appState, String message) {
//     TextEditingController _messageController = TextEditingController(text: message);

//     showDialog(
//       context: context,
//       builder: (context) {
//         return AlertDialog(
//           title: Text('Edit Message'),
//           content: TextField(
//             controller: _messageController,
//             decoration: InputDecoration(
//               hintText: 'Enter your message',
//             ),
//           ),
//           actions: [
//             TextButton(
//               onPressed: () {
//                 Navigator.pop(context);
//               },
//               child: Text('Cancel'),
//             ),
//             ElevatedButton(
//               onPressed: () {
//                 appState.editMessage(message, _messageController.text);
//                 Navigator.pop(context);
//               },
//               child: Text('Save'),
//             ),
//           ],
//         );
//       },
//     );
//   }
// }

import 'package:english_words/english_words.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget { //this app is a widget
  const MyApp({Key? key});
  
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => MyAppState(), //App state is changed here and provided by ChangeNotifierProvider
      child: MaterialApp(
        title: 'Namer App',
        theme: ThemeData(
          useMaterial3: true,
          colorScheme: ColorScheme.fromSeed(seedColor: Color.fromARGB(255, 77, 15, 212)),
        ),
        home: MyHomePage(),
      ),
    );
  }
}

class MyAppState extends ChangeNotifier {
  var current = WordPair.random();
  var favorites = <WordPair>[];
  var messages = <String>[];

  void getNext() {
    current = WordPair.random();
    notifyListeners();
  }

  void toggleFavorite() {
    if (favorites.contains(current)) {
      favorites.remove(current);
    } else {
      favorites.add(current);
    }
    notifyListeners();
  }

  void addMessage(String message) {
    messages.add(message);
    notifyListeners();
  }
  
  void editMessage(String oldMessage, String newMessage) {
    final index = messages.indexOf(oldMessage);
    if (index != -1) {
      messages[index] = newMessage;
      notifyListeners();
    }
  }
}

class MyHomePage extends StatefulWidget {
  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  var selectedIndex = 0;

  @override
  Widget build(BuildContext context) {
    Widget page;
    switch (selectedIndex) {
      case 0:
        page = GeneratorPage();
        break;
      //For when we add new pages
      case 1:
        page = FavoritesPage();
        break;
      default: 
        throw UnimplementedError('no widget for $selectedIndex');
    }

    return Scaffold(
      body: Row(
        children: [
          SafeArea(
            child: NavigationRail(
              extended: false,
              destinations: [
                NavigationRailDestination(
                  icon: Icon(Icons.home),
                  label: Text('Home'),
                ),
                NavigationRailDestination(
                  icon: Icon(Icons.favorite),
                  label: Text('Favorites'),
                ),
              ],
              selectedIndex: selectedIndex, // must be either 0 or 1 because there are only two destinations
              onDestinationSelected: (value) {
                setState(() {
                  selectedIndex = value; //says the selected index
                });
              },
            ),
          ),
          Expanded(
            child: Container(
              color: Theme.of(context).colorScheme.primaryContainer,
              child: page, //child is the entire generator page
            ),
          ),
        ],
      ),
    );
  }
}

class GeneratorPage extends StatelessWidget {
  final TextEditingController _messageController = TextEditingController();
  @override
  Widget build(BuildContext context) {
    var appState = context.watch<MyAppState>();
    var pair = appState.current;

    IconData icon;
    if (appState.favorites.contains(pair)) {
      icon = Icons.favorite;
    } else {
      icon = Icons.favorite_border;
    }

    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          TextField(
            controller: _messageController,
            decoration: InputDecoration(
              hintText: 'Enter your message',
              border: OutlineInputBorder(),
            ),
          ),
          SizedBox(height: 10),
          ElevatedButton(
            onPressed: () {
              appState.addMessage(_messageController.text);
              _messageController.clear();
            },
            child: Text('Save Message'),
          ),
          SizedBox(height: 10),
          BigCard(pair: pair),
          SizedBox(height: 10),
          Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              ElevatedButton.icon(
                onPressed: () {
                  appState.toggleFavorite();
                },
                icon: Icon(icon),
                label: Text('Like'),
              ),
              SizedBox(width: 10),
              ElevatedButton(
                onPressed: () {
                  appState.getNext();
                },
                child: Text('Next'),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class BigCard extends StatelessWidget {
  const BigCard({
    Key? key,
    required this.pair,
  }) : super(key: key);

  final WordPair pair;

  @override
  Widget build(BuildContext context) {
    var theme = Theme.of(context);
    var style = theme.textTheme.displayMedium!.copyWith(
      color: theme.colorScheme.onSecondary,
    );


    return Card(
      color: theme.colorScheme.primary,
      child: Padding(
        padding: const EdgeInsets.all(10),
        child: Text(pair.asLowerCase, 
        style: style,
        semanticsLabel: pair.asPascalCase,
        ),
      ),
    );
  }
}


class FavoritesPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var appState = context.watch<MyAppState>();

    if (appState.favorites.isEmpty && appState.messages.isEmpty) {
      return Center(
        child: Text('No favorites or messages yet.'),
      );
    }

    return ListView(
      children: [
        if (appState.favorites.isNotEmpty)
          Padding(
            padding: const EdgeInsets.all(20),
            child: Text('You have ${appState.favorites.length} favorites:'),
          ),
        for (var pair in appState.favorites)
          ListTile(
            leading: Icon(Icons.favorite),
            title: Text(pair.asLowerCase),
          ),
        if (appState.messages.isNotEmpty)
          Padding(
            padding: const EdgeInsets.all(20),
            child: Text('Your saved messages:'),
          ),
        for (var message in appState.messages)
          ListTile(
            leading: Icon(Icons.message),
            title: Text(message),
            trailing: IconButton(
              icon: Icon(Icons.edit),
              onPressed: () {
                _editMessage(context, appState, message);
              },
            ),
          ),
      ],
    );
  }

  void _editMessage(BuildContext context, MyAppState appState, String message) {
    TextEditingController messageController = TextEditingController(text: message);

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text('Edit Message'),
          content: TextField(
            controller: messageController,
            decoration: InputDecoration(
              hintText: 'Enter your message',
            ),
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () {
                appState.editMessage(message, messageController.text);
                Navigator.pop(context);
              },
              child: Text('Save'),
            ),
          ],
        );
      },
    );
  }
}
