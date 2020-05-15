import React from 'react';
import {CashPaidThing} from "../../../utils/types";
import PropTypes from "prop-types";
import SelectBox from "../../../utils/components/SelectBox";
import ItemContainer from "../../../utils/components/Container/ItemContainer";

const PaidThingItem = ({cashCategories, paidThing, onChange}) => {
    return <ItemContainer>
        <SelectBox onChange={onChange}
                   labelText={paidThing.paidThing.name}
                   value={paidThing.cashCategory}
                   items={cashCategories}/>
    </ItemContainer>
};

PaidThingItem.propTypes = {
    paidThing: CashPaidThing,
    onChange: PropTypes.func.isRequired
};

export default PaidThingItem;