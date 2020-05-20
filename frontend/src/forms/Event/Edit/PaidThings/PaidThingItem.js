import React from 'react';
import PropTypes from "prop-types";

import {CashPaidThing} from "forms/utils/types";
import SelectBox from "forms/utils/components/SelectBox";
import ItemContainer from "forms/utils/components/Container/ItemContainer";

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