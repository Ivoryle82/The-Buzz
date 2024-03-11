import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => MyAppState(),
      child: MaterialApp(
        title: 'Message App',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: MyHomePage(),
      ),
    );
  }
}

class MyAppState extends ChangeNotifier {
  var messages = <String>[];
  var favorites = <String>[];

  void addMessage(String message) {
    messages.add(message);
    notifyListeners();
  }

  void toggleFavorite(String message) {
    if (favorites.contains(message)) {
      favorites.remove(message);
    } else {
      favorites.add(message);
    }
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

class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Messages'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Expanded(
              child: MessagesList(),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => AddMessageScreen()),
          );
        },
        tooltip: 'Add Message',
        child: Icon(Icons.add),
      ),
    );
  }
}

class MessagesList extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var appState = context.watch<MyAppState>();

    return ListView.builder(
      itemCount: appState.messages.length,
      itemBuilder: (context, index) {
        final message = appState.messages[index];
        final isFavorite = appState.favorites.contains(message);
        return ListTile(
          title: Text(message),
          trailing: IconButton(
            icon: Icon(
              isFavorite ? Icons.favorite : Icons.favorite_border,
              color: isFavorite ? Colors.red : null,
            ),
            onPressed: () {
              appState.toggleFavorite(message);
            },
          ),
          onTap: () {
            _editMessage(context, appState, message);
          },
        );
      },
    );
  }
}

class AddMessageScreen extends StatelessWidget {
  final TextEditingController _messageController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    var appState = context.watch<MyAppState>();

    return Scaffold(
      appBar: AppBar(
        title: Text('Add Message'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            TextField(
              controller: _messageController,
              decoration: InputDecoration(
                hintText: 'Enter your message',
              ),
            ),
            SizedBox(height: 16.0),
            ElevatedButton(
              onPressed: () {
                final message = _messageController.text;
                if (message.isNotEmpty) {
                  appState.addMessage(message);
                  Navigator.pop(context);
                }
              },
              child: Text('Add'),
            ),
          ],
        ),
      ),
    );
  }
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
