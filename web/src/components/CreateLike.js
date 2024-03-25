// components/CreateLike.js
import React, { useState } from 'react';
import axios from 'axios';

function CreateLike() {
  const [title, setTitle] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    axios.post('http://your-backend-api/likes', { title })
      .then(response => console.log('Like created:', response.data))
      .catch(error => console.error('Error creating like:', error));
  };

  return (
    <div>
      <h1>Create Like</h1>
      <form onSubmit={handleSubmit}>
        <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} />
        <button type="submit">Submit</button>
      </form>
    </div>
  );
}

export default CreateLike;
