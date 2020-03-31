import React from 'react'
import PropTypes from 'prop-types'
import CloseIcon from '@material-ui/icons/Close';

const DeleteIcon = ({onDelete}) =>
    <CloseIcon onClick={onDelete} className='close-icon'/>;

DeleteIcon.propTypes = {
    onDelete: PropTypes.func.isRequired
};

export default DeleteIcon;