// components/LikeList.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function LikeList() {
  const [likes, setLikes] = useState([]);

  useEffect(() => {
    axios.get('http://your-backend-api/likes')
      .then(response => setLikes(response.data))
      .catch(error => console.error('Error fetching likes:', error));
  }, []);

  return (
    <div>
      <h1>Likes</h1>
      <ul>
        {likes.map(like => (
          <li key={like.id}>{like.title}</li>
        ))}
      </ul>
    </div>
  );
}

export default LikeList;
