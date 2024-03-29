import React, { useState, useEffect } from 'react';
import Header from '../../components/Header';
function Messages() {
  const [messages, setMessages] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchMessages();
  }, []);

  const fetchMessages = async () => {
    try {
      const response = await fetch('http://localhost:4567/messages');
      if (response.ok) {
        const data = await response.json();
        setMessages(data.payload); // Assuming the messages are stored in the 'payload' property of the response
      } else {
        setError('Failed to fetch messages');
      }
    } catch (error) {
      console.error('Error:', error);
      setError('An error occurred while fetching messages');
    }
  };

  return (
    <div>
      <div>
        <h2>Messages</h2>
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <ul>
          {messages.map((message, index) => (
            <li key={index}>{message}</li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default Messages;
