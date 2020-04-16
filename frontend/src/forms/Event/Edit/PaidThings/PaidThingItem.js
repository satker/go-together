import React from 'react';
import {CashPaidThing} from "../../../utils/types";
import PropTypes from "prop-types";
import SelectBox from "../../../utils/components/SelectBox";
import ContainerColumn from "../../../utils/components/Container/ContainerColumn";

const PaidThingItem = ({cashCategories, paidThing, onChange}) => {
    return <ContainerColumn style={{width: '300px'}}>
        {paidThing.paidThing.name} => <SelectBox onChange={onChange}
                                                 labelText={'cashCategory'}
                                                 value={paidThing.cashCategory}
                                                 items={cashCategories}/>
    </ContainerColumn>
};

PaidThingItem.propTypes = {
    paidThing: CashPaidThing,
    onChange: PropTypes.func.isRequired
};

export default PaidThingItem;