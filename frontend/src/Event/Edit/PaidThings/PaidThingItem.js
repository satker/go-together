import React from 'react';
import {CashPaidThing} from "../../../types";
import PropTypes from "prop-types";
import SelectBox from "../../../utils/components/SelectBox";

const PaidThingItem = ({cashCategories, paidThing, onChange}) => {
    return <div className='container-main-info-item fixed-width-content-paid-things'>
        {paidThing.paidThing.name} => <SelectBox onChange={onChange}
                                                 labelText={'cashCategory'}
                                                 value={paidThing.cashCategory}
                                                 items={cashCategories}/>
    </div>
};

PaidThingItem.propTypes = {
    paidThing: CashPaidThing,
    onChange: PropTypes.func.isRequired
};

export default PaidThingItem;