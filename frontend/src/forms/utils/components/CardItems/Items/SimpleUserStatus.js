import React from "react";
import {EventUser} from "../../../types";
import DeleteIcon from "../../Icon/Delete";
import PropTypes from "prop-types";
import AcceptIcon from "../../Icon/Accept";
import MessageIcon from "../../Icon/Message";
import {getSrcForImg} from "../../../utils";
import {Card, CardActions, CardContent, CardMedia, Typography} from "@material-ui/core";

const SimpleUserStatus = ({userEvent, onDelete, onClick, onAction}) => {
    return <Card style={{align: 'center', flexDirection: 'column'}}>
        <CardContent className='simple-user-item form-group'>
            <CardMedia className='flex'
                       style={{width: '60px', height: '60px'}}
                       component="img"
                       image={getSrcForImg(userEvent.user.userPhoto)}/>
            <CardContent className='flex'>
                <Typography>Login: {userEvent.user.login}</Typography>
                <Typography>Name: {userEvent.user.firstName}, {userEvent.user.lastName}</Typography>
            </CardContent>
        </CardContent>
        <CardActions className='simple-user-item'>
            {userEvent.userStatus !== 'APPROVED' && <AcceptIcon onAccept={() => onClick(userEvent.user.id)}/>}
            <MessageIcon onAction={() => onAction(userEvent.user.id)}/>
            {userEvent.userStatus !== 'REJECTED' && <DeleteIcon onDelete={() => onDelete(userEvent.user.id)}/>}
        </CardActions>
    </Card>;
};

SimpleUserStatus.propTypes = {
    userEvent: EventUser.isRequired,
    onDelete: PropTypes.func.isRequired,
    onClick: PropTypes.func.isRequired,
    onAction: PropTypes.func.isRequired
};

export default SimpleUserStatus;