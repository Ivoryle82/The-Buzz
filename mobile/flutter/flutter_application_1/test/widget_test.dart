import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:provider/provider.dart';
import 'package:flutter_application_1/main.dart';
import 'package:test/test.dart';

void main() {
  group('MyApp', () {
    testWidgets('Initial widget test', (WidgetTester tester) async {
      await tester.pumpWidget(MyApp());

      // Verify that the initial widget is MaterialApp
      expect(find.byType(MaterialApp), findsOneWidget);
    });
  });

  group('MyAppState', () {
    test('Adding and toggling favorites', () {
      final appState = MyAppState();
      final message = 'Test Message';

      // Add a message
      appState.addMessage(message);
      expect(appState.messages.length, 1);
      expect(appState.messages[0], message);

      // Toggle favorite status
      appState.toggleFavorite(message);
      expect(appState.favorites.length, 1);
      expect(appState.favorites[0], message);

      // Toggle favorite status again
      appState.toggleFavorite(message);
      expect(appState.favorites.length, 0);
    });

    test('Editing message', () {
      final appState = MyAppState();
      final oldMessage = 'Old Message';
      final newMessage = 'New Message';

      // Add a message
      appState.addMessage(oldMessage);
      expect(appState.messages.length, 1);
      expect(appState.messages[0], oldMessage);

      // Edit message
      appState.editMessage(oldMessage, newMessage);
      expect(appState.messages.length, 1);
      expect(appState.messages[0], newMessage);
    });
  });

  group('MyHomePage', () {
    testWidgets('Add message button test', (WidgetTester tester) async {
      await tester.pumpWidget(
        MaterialApp(
          home: ChangeNotifierProvider(
            create: (context) => MyAppState(),
            child: MyHomePage(),
          ),
        ),
      );

      // Tap the floating action button
      await tester.tap(find.byType(ElevatedButton));
      await tester.pump();

      // Verify that Add Message screen is opened
      expect(find.text('Save Message'), findsOneWidget);
    });
  });
}
