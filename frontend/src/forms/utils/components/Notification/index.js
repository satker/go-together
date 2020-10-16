import React from "react";
import Snackbar from '@material-ui/core/Snackbar';

import {hideNotification} from "./actions";
import {connect} from "App/Context";
import DeleteIcon from "../Icon/Delete";

const Notification = ({notification, hideNotification}) => {
    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        hideNotification();
    };

    return <Snackbar
        anchorOrigin={{
            vertical: 'top',
            horizontal: 'right',
        }}
        open={notification.isOpen}
        autoHideDuration={5000}
        onClose={handleClose}
        message={notification.message}
        action={
            <DeleteIcon onDelete={handleClose}/>
        }
    />
}

const mapStateToProps = (state) => ({
    notification: state.components.utils.notifications.notification.value
});

export default connect(mapStateToProps, {hideNotification})(Notification)