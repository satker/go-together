import React from 'react';
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
import {Card, CardActionArea, CardActions, CardContent, CardMedia, Typography} from "@material-ui/core";

const EventLikes = createEventLikes(FORM_ID);

const ItemEvent = ({event, onClickChooseEvent, onDelete, userId, eventIds}) => {
    return <Card>
        <CardActionArea>
            <DeleteIcon onDelete={() => onDelete(event.id)}/>
            <CardMedia
                component="img"
                height="250"
                image={getSrcForImg(event.eventPhotoDto.photos[0] || {...PHOTO_OBJECT})}
            />
            <CardContent>
                <Typography>{event.name}</Typography>
                <Typography>{event.description}</Typography>
                <Typography>I {event.author.firstName}, {event.author.lastName}</Typography>
                <Typography>From {event.author.location.name}, {event.author.location.country.name}</Typography>
                <Typography>Languages: {event.author.languages.map(lang => lang.name).join(', ')}</Typography>
                <Typography>My
                    interests: {event.author.interests.map(interest => interest.name).join(', ')}</Typography>
                <Typography>Going to travel through {event.route.map(location => location.location.name + ", " +
                    location.location.country.name).join(" -> ")}</Typography>
                <Typography>With {event.peopleCount} friends</Typography>
                <Typography>{userId && userId !== event.author.id &&
                <EventLikes eventId={event.id} eventIds={eventIds}/>}
                </Typography>
                <Typography>Live by {event.housingType}</Typography>
            </CardContent>
        </CardActionArea>
        <CardActions>
            <FormReference formRef={'/events/' + event.id}
                           action={() => onClickChooseEvent(event)}
                           description='Choose event'/>
            {event.eventPhotoDto.photos.length !== 0 &&
            <Gallery
                images={event.eventPhotoDto.photos.map(photo => getSrcForImg(photo))}
                showThumbnails={true}
            />}
        </CardActions>
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