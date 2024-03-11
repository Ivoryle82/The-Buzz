import * as React from 'react'
import * as ReactDOM from "react-dom";
import { BrowserRouter as Router, Route } from "react-router-dom";
import { useState, useEffect } from "react";
import LikeButton from './LikeButton';
import PostMessage from './PostMessage';
const backendUrl =  "https://2024sp-tutorial-exh226.dokku.cse.lehigh.edu";


function App (){
  

  const [getResult, setGetResult] = useState([]);

  const fetchPosts = async () => {
    try {
      const response = await fetch(`${backendUrl}/messages`);
      const data = await response.json();
      setGetResult(data);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  useEffect(() => {
    fetchPosts()
  }, []);


  
  
    return (
        //<h1>Hello World!</h1>
        <div id = "app">
            <LikeButton />
            <PostMessage />
        </div>


    );

}

/* export class App extends React.Component{
  
    //render(){


    /*return (
    <Router>
      <div className="container">
        <h1>NakedMoleRat</h1>
        
      </div>
    </Router>
  );
  
 //}
}
*/
export default App;