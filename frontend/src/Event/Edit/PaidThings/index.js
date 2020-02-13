import React, {useContext, useEffect, useState} from 'react';
import {Event} from "../../../types";
import PropTypes from "prop-types";
import PaidThingItem from "./PaidThingItem";
import {DEFAULT_PAID_THING, EVENTS_URL} from "../../../utils/constants";
import {Context} from "../../../Context";

const PaidThings = ({event, onChange}) => {
    const [payedThings, setPayedThings] = useState([]);
    const [cashCategories, setCashCategories] = useState([]);
    const [state] = useContext(Context);

    useEffect(() => {
        state.fetchWithToken(EVENTS_URL + '/cashCategories', setCashCategories);
        state.fetchWithToken(EVENTS_URL + '/payedThings', result => {
            const newPaidThings = [];
            for (const paidThing of result) {
                const newElement = {...DEFAULT_PAID_THING};
                newElement.paidThing = paidThing;
                newPaidThings.push(newElement);
            }
            setPayedThings(newPaidThings);
            onChange(newPaidThings, 'paidThings');
        })
    }, [state]);

    const onChangePaidThing = (arrayIndex) => (value) => {
        let oldArray = [...event.paidThings];
        oldArray[arrayIndex].cashCategory = value;
        onChange(oldArray, 'paidThings');
    };

    return <div className='flex-column'>
        <div className='flex'>
            Choose paid thing:
        </div>
        {payedThings.map((paidThing, index) =>
            <PaidThingItem
                cashCategories={cashCategories}
                paidThing={paidThing}
                onChange={onChangePaidThing(index)}/>)}
    </div>;
};

PaidThings.propTypes = {
    event: Event.isRequired,
    onChange: PropTypes.func.isRequired
};

export default PaidThings;