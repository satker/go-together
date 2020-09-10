import React from 'react'
import PropTypes from 'prop-types'
import ArrowForwardIcon from '@material-ui/icons/ArrowForward';

import './style.css'

const RightForward = ({onClick}) =>
    <ArrowForwardIcon onClick={onClick} className='arrow-icon'/>;

RightForward.propTypes = {
    onClick: PropTypes.func.isRequired
};

export default RightForward;