import React, {useEffect} from 'react';
import {Event, ResponseData} from "../../../utils/types";
import PropTypes from "prop-types";
import PaidThingItem from "./PaidThingItem";
import {DEFAULT_PAID_THING} from "../../../utils/constants";
import {connect} from "../../../../App/Context";
import {getCashCategories, getPayedThings} from "./actions";
import {updateEvent} from "../actions";
import Container from "../../../utils/components/Container/ContainerRow";
import ItemContainer from "../../../utils/components/Container/ItemContainer";

const PaidThings = ({
                        event, updateEvent, cashCategories, payedThings,
                        getCashCategories, getPayedThings
                    }) => {
    useEffect(() => {
        getCashCategories();
    }, [getCashCategories]);

    useEffect(() => {
        getPayedThings();
    }, [getPayedThings]);

    useEffect(() => {
        if (event.paidThings.length === 0 &&
            !payedThings.inProcess && payedThings.response.length !== 0) {
            const newPaidThings = [];
            for (const paidThing of payedThings.response) {
                const newElement = {...DEFAULT_PAID_THING};
                newElement.paidThing = paidThing;
                newPaidThings.push(newElement);
            }
            updateEvent('paidThings', newPaidThings);
        }
    }, [event, payedThings, updateEvent]);

    const onChangePaidThing = (arrayIndex) => (value) => {
        let oldArray = [...event.paidThings];
        oldArray[arrayIndex].cashCategory = value;
        updateEvent('paidThings', oldArray);
    };

    return <Container>
        <ItemContainer>
            Choose paid thing:
        </ItemContainer>
        {event.paidThings.map((paidThing, index) =>
            <PaidThingItem
                key={index}
                cashCategories={cashCategories.response}
                paidThing={paidThing}
                onChange={onChangePaidThing(index)}/>)}
    </Container>;
};

PaidThings.propTypes = {
    event: Event.isRequired,
    updateEvent: PropTypes.func.isRequired,
    getCashCategories: PropTypes.func.isRequired,
    getPayedThings: PropTypes.func.isRequired,
    cashCategories: ResponseData.isRequired,
    payedThings: ResponseData.isRequired
};

const mapStateToProps = () => (state) => ({
    cashCategories: state.components.forms.event.eventEdit.paidThings.cashCategories,
    payedThings: state.components.forms.event.eventEdit.paidThings.payedThings,
    event: state.components.forms.event.eventEdit.event.response
});

export default connect(mapStateToProps, {getCashCategories, getPayedThings, updateEvent})(PaidThings);