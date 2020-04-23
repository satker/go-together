import SelectBox from "../../../../utils/components/SelectBox";
import CheckInOutDates from "../../../../utils/components/CheckInOutDates";
import React, {useEffect} from "react";
import {FORM_ID} from "../../constants";
import {getHousingTypes} from "../actions";
import {connect} from "../../../../../App/Context";
import {Event, ResponseData} from "../../../../utils/types";
import PropTypes from "prop-types";
import LabeledInput from "../../../../utils/LabeledInput";

const CommonInfo = ({event, onChangeEvent, getHousingTypes, housingTypes}) => {
    useEffect(() => {
        getHousingTypes();
    }, [getHousingTypes]);

    return <>
        <LabeledInput
            id="name"
            label="Event name"
            value={event.name}
            onChange={(value) => onChangeEvent('name', value)}
        />
        <SelectBox onChange={(value) => onChangeEvent('housingType', housingTypes.response
            .filter(type => type.id === value)[0].id)}
                   labelText='Housing type'
                   value={event.housingType}
                   items={housingTypes.response}/>
        <LabeledInput
            id="description"
            label="Description"
            value={event.description}
            defaultValue="Home, dear home..."
            onChange={(value) => onChangeEvent('description', value)}
        />
        <LabeledInput
            type='number'
            id="peopleCount"
            label="People count"
            value={event.peopleCount}
            onChange={(value) => onChangeEvent('peopleCount', value)}
        />
        Trip dates: <CheckInOutDates startDate={event.startDate}
                                     endDate={event.endDate}
                                     setStartDate={startDate => onChangeEvent('startDate', startDate)}
                                     setEndDate={endDate => onChangeEvent('endDate', endDate)}
    />
    </>
};

CommonInfo.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired,
    housingTypes: ResponseData,
    getHousingTypes: PropTypes.func.isRequired
};

const mapStateToProps = (FORM_ID) => (state) => ({
    housingTypes: state[FORM_ID]?.housingTypes || []
});

export default connect(mapStateToProps, {getHousingTypes})(CommonInfo)(FORM_ID);