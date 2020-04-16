import React, {useEffect} from 'react';
import {Event} from "../../../utils/types";
import PropTypes from "prop-types";
import PaidThingItem from "./PaidThingItem";
import {DEFAULT_PAID_THING} from "../../../utils/constants";
import {connect} from "../../../../App/Context";
import {FORM_ID} from "../constants";
import {getCashCategories, getPayedThings} from "./actions";

const PaidThings = ({
                        event, onChangeEvent, cashCategories, payedThings,
                        getCashCategories, getPayedThings
                    }) => {
    useEffect(() => {
        getCashCategories();
    }, [getCashCategories]);

    useEffect(() => {
        if (payedThings.length !== 0) {
            const newPaidThings = [];
            for (const paidThing of payedThings) {
                const newElement = {...DEFAULT_PAID_THING};
                newElement.paidThing = paidThing;
                newPaidThings.push(newElement);
            }
            onChangeEvent('paidThings', newPaidThings);
        }
    }, [payedThings]);

    const onChangePaidThing = (arrayIndex) => (value) => {
        let oldArray = [...event.paidThings];
        oldArray[arrayIndex].cashCategory = value;
        onChangeEvent('paidThings', oldArray);
    };

    return <div className='flex-column'>
        <div className='flex'>
            Choose paid thing:
        </div>
        {event.paidThings.map((paidThing, index) =>
            <PaidThingItem
                cashCategories={cashCategories}
                paidThing={paidThing}
                onChange={onChangePaidThing(index)}/>)}
    </div>;
};

PaidThings.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired,
    getCashCategories: PropTypes.func.isRequired,
    getPayedThings: PropTypes.func.isRequired,
    cashCategories: PropTypes.array.isRequired,
    payedThings: PropTypes.array.isRequired
};

const mapStateToProps = (state) => ({
    cashCategories: state[FORM_ID]?.cashCategories || [],
    payedThings: state[FORM_ID]?.payedThings
});

export default connect(mapStateToProps, {getCashCategories, getPayedThings}, FORM_ID)(PaidThings);