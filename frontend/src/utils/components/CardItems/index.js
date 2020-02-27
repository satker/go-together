import React, {useContext, useEffect, useState} from 'react';
import PropTypes from "prop-types";
import {Card} from "reactstrap";
import DeleteButton from "../DeleteButton/DeleteButton";
import {getSrcForImg} from "../../utils";
import ItemEvent from "./ItemEvent";
import {Context} from "../../../Context";

const GroupItems = ({
                        onDelete, items, editable, isEvents, onClick,
                        isPhotos, onChange, countRowItems
                    }) => {
    const [parsedCards, setParsedCards] = useState([]);
    const [state] = useContext(Context);

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
            }
            setParsedCards(parseItems);
        }
    }, [items, setParsedCards, isPhotos, state.countRowItems,
        editable, onDelete, onChange, isEvents, onClick, countRowItems]);

    return parsedCards && <div className='container-cards'>
        {parsedCards.map(item => <div className='container-cards-item margin-left-item'>
            {item}
        </div>)}
    </div>
};

GroupItems.propTypes = {
    items: PropTypes.array.isRequired,
    editable: PropTypes.bool,
    isPhotos: PropTypes.bool,
    isEvents: PropTypes.bool,
    onClick: PropTypes.func,
    onDelete: PropTypes.func,
    countRowItems: PropTypes.number
};

export default GroupItems;

const mapPhotos = (photos, onDelete, key) => photos.map((photo) =>
    <div className='flex'>
        <Card key={key + photo.id}>
            <DeleteButton onDelete={() => onDelete(photo.id)}/>
            <img className='fixed-width-min' src={getSrcForImg(photo)} alt=''/>
        </Card>
    </div>);

const mapEvents = (events, onClick, onDelete, key) =>
    events.map(event =>
        <ItemEvent
            onDelete={onDelete}
            key={key + event.id}
            event={event}
            onClickChooseApartment={onClick}
        />);