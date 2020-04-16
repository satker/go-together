import React from "react";
import {Card} from "reactstrap";
import {EventUser} from "../../../types";
import SimpleUserCard from "./SimpleUserCard";
import DeleteIcon from "../../Icon/Delete";
import PropTypes from "prop-types";
import AcceptIcon from "../../Icon/Accept";
import MessageIcon from "../../Icon/Message";

const SimpleUserStatus = ({userEvent, onDelete, onClick, onAction}) => {
    return <Card body style={{align: 'center', flexDirection: 'column'}}>
        <SimpleUserCard user={userEvent.user}/>
        <div className='simple-user-item'>
            {userEvent.userStatus !== 'APPROVED' && <AcceptIcon onAccept={() => onClick(userEvent.user.id)}/>}
            <MessageIcon onAction={() => onAction(userEvent.user.id)}/>
            {userEvent.userStatus !== 'REJECTED' && <DeleteIcon onDelete={() => onDelete(userEvent.user.id)}/>}
        </div>
    </Card>;
};

SimpleUserStatus.propTypes = {
    userEvent: EventUser.isRequired,
    onDelete: PropTypes.func.isRequired,
    onClick: PropTypes.func.isRequired,
    onAction: PropTypes.func.isRequired
};

export default SimpleUserStatus;