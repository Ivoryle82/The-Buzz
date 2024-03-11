import * as React from 'react'
import * as ReactDOM from "react-dom";
import { BrowserRouter as Router, Route } from "react-router-dom";
import { useState, useEffect } from "react";
import LikeButton from './LikeButton';

const backendUrl =  "https://2024sp-tutorial-exh226.dokku.cse.lehigh.edu";


function PostMessage (){

  const [post, setPost] = useState('');

  const handlePost = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log({ post });
    addPost();
    setPost('');
  };    

  const entryList = [];

  const [posts, setPosts] = useState<{ mMessage: string }[]>([]);
    

    const fetchPosts = async() => {
        const response = await fetch(`${backendUrl}/messages`);
        const data = await response.json();
        setPosts(data);
    } 

    useEffect(() => {
        fetchPosts()
     }, []);
     

    const addPost = async() => {
      const response = await fetch(`${backendUrl}/messages`, {
        method: 'POST',
        body: JSON.stringify({
          
        }),
         headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
      });
        const data = await response.json();
        setPosts((prevPosts) => [data, ...prevPosts])
    };

  
    return (
      <div>
      <form onSubmit={handlePost}>
        <h2>Create Message Post</h2>
        <div className="input-container">
            <textarea 
                name="body" 
                value={post} 
                onChange={(e) => setPost(e.target.value)}>
            </textarea>
        </div>
        <button type="submit" className="btn-submit"> Post</button>
     </form>
        <div>
          <h2>Posts</h2>
          {posts.map((p, index) => (
            <div key={index} className="post">
              {p.mMessage}
            </div>
          ))}
        </div>
    </div>
    );

}


export default PostMessage;