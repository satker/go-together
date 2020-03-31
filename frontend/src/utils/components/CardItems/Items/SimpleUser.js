import React from "react";
import {Card, CardSubtitle, CardTitle} from "reactstrap";
import {Image} from "react-bootstrap";
import {getSrcForImg} from "../../../utils";
import {EventUser} from "../../../../types";
import DeleteIcon from "../../Icon/Delete";
import PropTypes from "prop-types";
import AcceptIcon from "../../Icon/Accept";
import MessageIcon from "../../Icon/Message";

const SimpleUser = ({user, onDelete, onClick, onAction}) => {
    return <Card body style={{align: 'center', flexDirection: 'column'}}>
        <div className='simple-user-item form-group'>
            <div className='flex'>
                <Image className='simple_user_img' src={getSrcForImg(user.user.userPhoto)}/>
            </div>
            <div className='flex'>
                <CardTitle>Login: {user.user.login}</CardTitle>
                <CardSubtitle>Name: {user.user.firstName}, {user.user.lastName}</CardSubtitle>
            </div>
        </div>

        <div className='simple-user-item'>
            {user.userStatus === 'APPROVED' && <AcceptIcon onAccept={() => onClick(user.user.id)}/>}
            <MessageIcon onAction={() => onAction(user.user.id)}/>
            <DeleteIcon onDelete={() => onDelete(user.user.id)}/>
        </div>
    </Card>;
};

SimpleUser.propTypes = {
    user: EventUser.isRequired,
    onDelete: PropTypes.func.isRequired,
    onClick: PropTypes.func.isRequired,
    onAction: PropTypes.func.isRequired
};

export default SimpleUser;