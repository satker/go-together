import React from 'react';
import {Card, CardBody, CardLink, CardText, CardTitle} from "reactstrap";
import Gallery from "../../Galery";
import PropTypes from "prop-types";
import {getSrcForImg} from "../../../utils";
import {Event} from "../../../types";
import FormReference from "../../FormReference";
import DeleteIcon from "../../Icon/Delete";
import {PHOTO_OBJECT} from "../../../constants";
import createEventLikes from "../../Event/EventLikes";
import {connect} from "../../../../../App/Context";
import {FORM_ID} from "../../../../Events/constants";

const EventLikes = createEventLikes(FORM_ID);

const ItemEvent = ({event, onClickChooseEvent, onDelete, userId, eventIds}) => {
    return <Card body style={{align: 'center'}}>
        <DeleteIcon onDelete={() => onDelete(event.id)}/>
        <img className='fixed-width-main-image-card'
             src={getSrcForImg(event.eventPhotoDto.photos[0] || {...PHOTO_OBJECT})} alt=""/>
        <CardBody>
            <CardTitle>{event.name}</CardTitle>
            <CardText>{event.description}</CardText>
            <CardText>I {event.author.firstName}, {event.author.lastName}</CardText>
            <CardText>From {event.author.location.name}, {event.author.location.country.name}</CardText>
            <CardText>Languages: {event.author.languages.map(lang => lang.name).join(', ')}</CardText>
            <CardText>My
                interests: {event.author.interests.map(interest => interest.name).join(', ')}</CardText>
            <CardText>Going to travel through {event.route.map(location => location.location.name + ", " +
                location.location.country.name).join(" -> ")}</CardText>
            <CardText>With {event.peopleCount} friends</CardText>
            <CardText>{userId && userId !== event.author.id &&
            <EventLikes eventId={event.id} eventIds={eventIds}/>}
            </CardText>
            <CardText>Live by {event.housingType}</CardText>
            <FormReference formRef={'/events/' + event.id}
                           action={() => onClickChooseEvent(event)}
                           description='Choose event'/>
            {event.eventPhotoDto.photos.length !== 0 &&
            <CardLink><Gallery
                images={event.eventPhotoDto.photos.map(photo => getSrcForImg(photo))}
                showThumbnails={true}
            /></CardLink>}
        </CardBody>
    </Card>;
};

ItemEvent.propTypes = {
    event: Event.isRequired,
    onClickChooseEvent: PropTypes.func.isRequired,
    onDelete: PropTypes.func,
    userId: PropTypes.string,
    eventIds: PropTypes.arrayOf(PropTypes.string).isRequired
};

const mapStateToProps = () => (state) => ({
    userId: state.userId
});

export default connect(mapStateToProps, null)(ItemEvent)(FORM_ID);