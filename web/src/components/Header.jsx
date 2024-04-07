import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Link, Route, Switch } from 'react-router-dom';
import Profile from '../pages/Profile'; // Import the Profile component
import { AppBar, Box, Button, Modal, TextField, Toolbar, Typography } from '@mui/material';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import FavoriteIcon from '@mui/icons-material/Favorite';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  pt: 2,
  px: 4,
  pb: 3,
};

function Header() {
  const [openModal, setOpenModal] = useState(false);
  const [editModalOpen, setEditModal] = useState(false);
  const [titleInput, setTitleInput] = useState('');
  const [userInput, setUserInput] = useState('');
  const [editIndex, setEditIndex] = useState(null);
  const [messages, setMessages] = useState([]);
  const [liked, setLiked] = useState([]);
// Functionalities and switches
  useEffect(() => {
    fetchMessages();
  }, []);

  useEffect(() => {
    setLiked(Array(messages.length).fill(false));
  }, [messages]);

  const fetchMessages = async () => {
    try {
      const response = await fetch('https://team-goku.dokku.cse.lehigh.edu/messages');
      if (!response.ok) {
        throw new Error('Failed to fetch messages');
      }
      const data = await response.json();
      setMessages(data.mData);
    } catch (error) {
      console.error('Error fetching messages:', error);
    }
  };

  const handleOpenModal = () => {
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
  };

// App bar and modals 
  return (
    <div>
      <Box sx={{ flexGrow: 1 }}>
        <AppBar position="static" style={{ background: '#2E3B55' }}>
          <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              The Buzz
            </Typography>
            <Button color="inherit">Login</Button>
            <Button color="inherit">Sign Up</Button>
            <Button color="inherit" onClick={handleOpenModal}>Messages</Button>
            <Button color="inherit" component={Link} to="/profile">Profile</Button>
          </Toolbar>
        </AppBar>
      </Box>  
    </div>
  );
}

export default Header;
