import React, { useState } from 'react';

function AddMessage() {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch('http://localhost:4567/messages/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ mTitle: title, mContent: content }),
      });
      if (response.ok) {
        // Message added successfully
        // You can perform further actions here
      } else {
        setError('Failed to add message');
      }
    } catch (error) {
      console.error('Error:', error);
      setError('An error occurred while adding message');
    }
  };

  return (
    <div>
      <h2>Add Message</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <input type="text" placeholder="Title" value={title} onChange={(e) => setTitle(e.target.value)} />
        <textarea placeholder="Content" value={content} onChange={(e) => setContent(e.target.value)} />
        <button type="submit">Add Message</button>
      </form>
    </div>
  );
}

export default AddMessage;
