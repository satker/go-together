import React, {useContext, useState} from "react";
import {Button, FormGroup, Input, Label} from "reactstrap";
import {Context} from "../../../Context";
import {MESSAGE_SERVICE_URL} from "../../../utils/constants";
import PropTypes from "prop-types";

const URL_EVENT_REVIEWS = MESSAGE_SERVICE_URL + "/events/_id_/messages";

const InputComment = ({setReviewsByEvent, eventId}) => {
    const [message, setMessage] = useState(null);

    const [state] = useContext(Context);

    const textChanged = (evt) => {
        setMessage(evt.target.value);
    };

    const refresh = () => {
        setMessage(null);
        state.fetchWithToken(URL_EVENT_REVIEWS.replace("_id_", eventId),
            setReviewsByEvent);
    };

    const send = () => {
        const body = {
            message,
            author: {
                id: state.userId
            },
            recipientId: eventId
        };

        state.fetchWithToken(URL_EVENT_REVIEWS.replace("_id_", eventId), () => refresh(), 'PUT', body);
    };
    return <>
        <FormGroup>
            <Label xl={20} for="exampleSelectMulti"><h4>Leave your own review</h4></Label>
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
    setReviewsByEvent: PropTypes.func.isRequired,
    eventId: PropTypes.string.isRequired
};

export default InputComment;