import React from 'react'
import {CardText} from "reactstrap";
import PropTypes from 'prop-types'

const DeleteButton = ({onDelete}) => <CardText className='text-right'>
    <button type="button" className="close" aria-label="Close" onClick={onDelete}>
        <span aria-hidden="true">&times;</span>
    </button>
</CardText>;

DeleteButton.propTypes = {
    onDelete: PropTypes.func.isRequired
};

export default DeleteButton;