import React, {useEffect} from 'react';
import MainInfo from "./MainInfo";
import {connect} from "../../../App/Context";
import {Event, ResponseData} from "../../utils/types";
import {navigate} from 'hookrouter';
import PaidThings from "./PaidThings";
import Route from "./Route";
import Container from "../../utils/components/Container/ContainerRow";
import * as PropTypes from "prop-types";
import {postUpdatedEvent, putNewEvent, updateEvent} from "./actions";
import CustomButton from "../../utils/components/CustomButton";
import ItemContainer from "../../utils/components/Container/ItemContainer";

const EditForm = ({
                      event, userId, postUpdatedEvent, putNewEvent,
                      updatedEvent, newEvent
                  }) => {

    console.log(event);
    const saveEvent = () => {
        let saveObj = {...event};
        saveObj.id = event.id;
        saveObj.author = {
            id: userId
        };
        event.id ? postUpdatedEvent(saveObj) : putNewEvent(saveObj);
    };

    useEffect(() => {
        const id = updatedEvent.response.id || newEvent.response.id;
        if (id) {
            navigate('/events/' + id)
        }
    }, [updatedEvent, newEvent]);

    return <Container>
        <MainInfo/>
        <PaidThings/>
        <Route/>
        <ItemContainer>
            <CustomButton color="primary"
                          text='Save'
                          onClick={saveEvent}/>
        </ItemContainer>
    </Container>
};

EditForm.propTypes = {
    event: Event.isRequired,
    userId: PropTypes.string,
    postUpdatedEvent: PropTypes.func.isRequired,
    putNewEvent: PropTypes.func.isRequired,
    updatedEvent: ResponseData.isRequired,
    newEvent: ResponseData.isRequired
};

const mapStateToProps = () => (state) => ({
    userId: state.userId.value,
    updatedEvent: state.components.forms.event.eventEdit.updatedEvent,
    newEvent: state.components.forms.event.eventEdit.newEvent,
    event: state.components.forms.event.eventEdit.event.response
});

export default connect(mapStateToProps, {postUpdatedEvent, putNewEvent, updateEvent})(EditForm);