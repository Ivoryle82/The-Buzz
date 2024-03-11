import * as React from 'react'
import * as ReactDOM from "react-dom";
import { BrowserRouter as Router, Route } from "react-router-dom";
import { useState, useEffect } from "react";
import LikeButton from './LikeButton';
import axios from 'axios';

const backendUrl =  "https://2024sp-tutorial-exh226.dokku.cse.lehigh.edu";


function Entry (props){
    const [posts, setPosts] = useState<{ mMessage: string }[]>([]);
    

    const fetchPosts = async() => {
        const response = await fetch(`${backendUrl}/messages`);
        const data = await response.json();
        setPosts(data);
    } 

    useEffect(() => {
        fetchPosts()
     }, []);
     

    const addPost = async(body) => {
      const response = await fetch(`${backendUrl}/messages`, {
        method: 'POST',
        body: JSON.stringify({
            body: body,
        }),
         headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
      });
        const data = await response.json();
        setPosts((prevPosts) => [data, ...prevPosts])
    };

    const deletePost = async(id) => {
        const response = await fetch(`${backendUrl}/messages`, {
          method: 'DELETE'
        })

          if(response.status === 200) {
            setPosts(
              posts.filter((post) => {
                //return post.id !== id;
              })
            )
          }
      };
     


  
    return (
      <p>Hello World</p>


    );

}


export default Entry;