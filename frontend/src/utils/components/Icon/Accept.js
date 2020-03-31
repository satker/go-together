import React from 'react'
import PropTypes from 'prop-types'
import CheckIcon from '@material-ui/icons/Check';

const AcceptIcon = ({onAccept}) =>
    <CheckIcon onClick={onAccept} className='approve-icon'/>;

AcceptIcon.propTypes = {
    onAccept: PropTypes.func.isRequired
};

export default AcceptIcon;