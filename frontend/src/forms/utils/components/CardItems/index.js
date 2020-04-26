import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";
import Delete from "../Icon/Delete";
import {getSrcForImg} from "../../utils";
import ItemEvent from "./Items/Event";
import SimpleUserStatus from "./Items/SimpleUserStatus";
import './style.css'
import {Card, CardMedia} from "@material-ui/core";

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
                parseItems = mapEvents(items, onDelete, key)
            } else if (isUsers) {
                key = 'users_';
                parseItems = mapUsers(items, onClick, onDelete, onAction, key);
            }
            setParsedCards(parseItems);
        } else {
            setParsedCards([]);
        }
    }, [items, setParsedCards, isPhotos, onDelete, isEvents, onClick, isUsers, onAction]);

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
    <Card className='flex'>
        <Delete onDelete={() => onDelete(photo.id)}/>
        <CardMedia key={key + photo.id}
                   style={{width: '100px', height: '70px'}}
                   component="img"
                   image={getSrcForImg(photo)}>
        </CardMedia>
    </Card>);

const mapEvents = (events, onDelete, key) =>
    events.map(event =>
        <ItemEvent
            eventIds={events.map(event => event.id)}
            onDelete={onDelete}
            key={key + event.id}
            event={event}
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