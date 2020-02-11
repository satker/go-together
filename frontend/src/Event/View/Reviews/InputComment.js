import React, {useContext, useState} from "react";
import {Button, FormGroup, Input, Label} from "reactstrap";
import ReactStars from "react-stars";
import {Context} from "../../../Context";
import {MESSAGE_SERVICE_URL} from "../../../utils/constants";
import PropTypes from "prop-types";

const URL_APARTMENTS_REVIEWS = MESSAGE_SERVICE_URL + "/apartments/_id_/reviews";

const InputComment = ({setReviewsByApartment, eventId}) => {
    const [rating, setRating] = useState(null);
    const [message, setMessage] = useState(null);

    const [state] = useContext(Context);

    const ratingChanged = (newRating) => {
        setRating(newRating);
    };
    const textChanged = (evt) => {
        setMessage(evt.target.value);
    };

    const refresh = () => {
        setRating(null);
        setMessage(null);
        state.fetchWithToken(URL_APARTMENTS_REVIEWS.replace("_id_", eventId),
            setReviewsByApartment);
    };

    const send = () => {
        let body = {
            rating,
            message,
            userId: state.userId,
            eventId: eventId
        };

        state.fetchWithToken(MESSAGE_SERVICE_URL + '/apartments/review', () => refresh(), 'PUT', body);
    };
    return <>
        <FormGroup>
            <Label xl={20} for="exampleSelectMulti"><h4>Leave your own review</h4></Label>
            <ReactStars
                onChange={ratingChanged}
                value={rating}
                count={5}
                size={24}
                edit={true}
                color2={'#ffd700'}/>
            Enter text: <Input innerRef={el => el = message}
                               onChange={textChanged}
                               type="textarea"
                               name="text"
                               id="exampleText"/>
        </FormGroup>
        <Button onClick={send}>Rate</Button>
    </>
};

InputComment.propTypes = {
    setReviewsByApartment: PropTypes.func.isRequired,
    eventId: PropTypes.string.isRequired
};

export default InputComment;