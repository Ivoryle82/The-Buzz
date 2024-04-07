import React from 'react';
import Box from '@mui/material/Box';
import Fab from '@mui/material/Fab';
import AddIcon from '@mui/icons-material/Add';

const style = {
  position: 'fixed',
  bottom: 16,
  right: 16,
};

function FloatingActionButtons({ onClick }) {
  return (
    <Box sx={style}>
      <Fab color="primary" aria-label="add" onClick={onClick}>
        <AddIcon />
      </Fab>
    </Box>
  );
}

export default FloatingActionButtons;
