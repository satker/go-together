import SelectBox from "../../../../utils/components/SelectBox";
import CheckInOutDates from "../../../../utils/components/CheckInOutDates";
import React, {useEffect} from "react";
import {getHousingTypes} from "../actions";
import {connect} from "../../../../../App/Context";
import {Event, ResponseData} from "../../../../utils/types";
import PropTypes from "prop-types";
import LabeledInput from "../../../../utils/LabeledInput";
import ItemContainer from "../../../../utils/components/Container/ItemContainer";
import {updateEvent} from "../../actions";
import moment from "moment";

const CommonInfo = ({event, updateEvent, getHousingTypes, housingTypes}) => {
    useEffect(() => {
        getHousingTypes();
    }, [getHousingTypes]);

    return <>
        <ItemContainer>
            <LabeledInput
                id="name"
                label="Event name"
                value={event.name}
                onChange={(value) => updateEvent('name', value)}
            />
        </ItemContainer>
        <ItemContainer>
            <SelectBox onChange={(value) => updateEvent('housingType', housingTypes.response
                .filter(type => type.id === value)[0].id)}
                       labelText='Housing type'
                       value={event.housingType}
                       items={housingTypes.response}/>
        </ItemContainer>
        <ItemContainer>
            <LabeledInput
                id="description"
                label="Description"
                value={event.description}
                defaultValue="Home, dear home..."
                onChange={(value) => updateEvent('description', value)}
            />
        </ItemContainer>
        <ItemContainer>
            <LabeledInput
                type='number'
                id="peopleCount"
                label="People count"
                value={event.peopleCount}
                onChange={(value) => updateEvent('peopleCount', value)}
            />
        </ItemContainer>
        <ItemContainer>
            Trip dates: <CheckInOutDates startDate={event.startDate && moment(event.startDate)}
                                         endDate={event.endDate && moment(event.endDate)}
                                         setStartDate={startDate => updateEvent('startDate', startDate)}
                                         setEndDate={endDate => updateEvent('endDate', endDate)}
        />
        </ItemContainer>
    </>
};

CommonInfo.propTypes = {
    event: Event.isRequired,
    updateEvent: PropTypes.func.isRequired,
    housingTypes: ResponseData,
    getHousingTypes: PropTypes.func.isRequired
};

const mapStateToProps = () => (state) => ({
    housingTypes: state.components.forms.event.eventEdit.mainInfo.commonInfo.housingTypes,
    event: state.components.forms.event.eventEdit.event.response
});

export default connect(mapStateToProps, {getHousingTypes, updateEvent})(CommonInfo);