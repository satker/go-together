import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";
import {Card} from "reactstrap";
import Delete from "../Icon/Delete";
import {getSrcForImg} from "../../utils";
import ItemEvent from "./Items/Event";
import SimpleUserStatus from "./Items/SimpleUserStatus";

const GroupItems = ({onDelete, items, onClick, onAction, isEvents, isPhotos, isUsers}) => {
    const [parsedCards, setParsedCards] = useState([]);

    useEffect(() => {
        if (items) {
            let parseItems = [];
            let key = '';
            if (isPhotos) {
                key = 'photos_';
                parseItems = mapPhotos(items, onDelete, key)
            } else if (isEvents) {
                key = 'events_';
                parseItems = mapEvents(items, onClick, onDelete, key)
            } else if (isUsers) {
                key = 'users_';
                parseItems = mapUsers(items, onClick, onDelete, onAction, key);
            }
            setParsedCards(parseItems);
        } else {
            setParsedCards([]);
        }
    }, [items, setParsedCards, isPhotos, onDelete, isEvents, onClick, isUsers]);

    return parsedCards && <div className='container-cards'>
        {parsedCards.map((item, key) => <div key={key} className='container-cards-item margin-left-item'>
            {item}
        </div>)}
    </div>
};

GroupItems.propTypes = {
    items: PropTypes.array,
    isPhotos: PropTypes.bool,
    isEvents: PropTypes.bool,
    isUsers: PropTypes.bool,
    onClick: PropTypes.func,
    onDelete: PropTypes.func
};

export default GroupItems;

const mapPhotos = (photos, onDelete, key) => photos.map((photo) =>
    <div className='flex'>
        <Card key={key + photo.id}>
            <Delete onDelete={() => onDelete(photo.id)}/>
            <img className='fixed-width-min' src={getSrcForImg(photo)} alt=''/>
        </Card>
    </div>);

const mapEvents = (events, onClick, onDelete, key) =>
    events.map(event =>
        <ItemEvent
            onDelete={onDelete}
            key={key + event.id}
            event={event}
            onClickChooseEvent={onClick}
        />);

const mapUsers = (users, onClick, onDelete, onAction, key) =>
    users.map(user =>
        <SimpleUserStatus
            onDelete={onDelete}
            onAction={onAction}
            key={key + user.id}
            userEvent={user}
            onClick={onClick}
        />);