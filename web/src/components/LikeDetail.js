// components/LikeDetail.js
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

function LikeDetail() {
  const [like, setLike] = useState(null);
  const { id } = useParams();

  useEffect(() => {
    axios.get(`http://your-backend-api/likes/${id}`)
      .then(response => setLike(response.data))
      .catch(error => console.error('Error fetching like:', error));
  }, [id]);

  return (
    <div>
      {like ? (
        <div>
          <h1>{like.title}</h1>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
}

export default LikeDetail;
