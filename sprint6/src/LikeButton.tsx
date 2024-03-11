import * as React from 'react'
import * as ReactDOM from "react-dom";
import { BrowserRouter as Router, Route } from "react-router-dom";
import { useState, useEffect } from "react";
import "./style.css"

function LikeButton (){
  
  const count = 0;
  const [likes, setLikes] = useState(0);
  
  const [Liked, setLiked] = useState (false);


    return (
        //<h1>Hello World!</h1>
              <button onClick = {() => {setLikes(likes+1); setLiked(true)}
            }> {likes} Likes</button>
        


    );

}

export default LikeButton;