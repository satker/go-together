import SelectBox from "../../../../utils/components/SelectBox";
import CheckInOutDates from "../../../../utils/components/CheckInOutDates";
import React, {useEffect} from "react";
import {FORM_ID} from "../../constants";
import {getHousingTypes} from "../actions";
import {connect} from "../../../../../App/Context";
import {Event, ResponseData} from "../../../../utils/types";
import PropTypes from "prop-types";
import LabeledInput from "../../../../utils/LabeledInput";
import ItemContainer from "../../../../utils/components/Container/ItemContainer";

const CommonInfo = ({event, onChangeEvent, getHousingTypes, housingTypes}) => {
    useEffect(() => {
        getHousingTypes();
    }, [getHousingTypes]);

    return <>
        <ItemContainer>
            <LabeledInput
                id="name"
                label="Event name"
                value={event.name}
                onChange={(value) => onChangeEvent('name', value)}
            />
        </ItemContainer>
        <ItemContainer>
            <SelectBox onChange={(value) => onChangeEvent('housingType', housingTypes.response
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
                onChange={(value) => onChangeEvent('description', value)}
            />
        </ItemContainer>
        <ItemContainer>
            <LabeledInput
                type='number'
                id="peopleCount"
                label="People count"
                value={event.peopleCount}
                onChange={(value) => onChangeEvent('peopleCount', value)}
            />
        </ItemContainer>
        <ItemContainer>
            Trip dates: <CheckInOutDates startDate={event.startDate}
                                         endDate={event.endDate}
                                         setStartDate={startDate => onChangeEvent('startDate', startDate)}
                                         setEndDate={endDate => onChangeEvent('endDate', endDate)}
        />
        </ItemContainer>
    </>
};

CommonInfo.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired,
    housingTypes: ResponseData,
    getHousingTypes: PropTypes.func.isRequired
};

const mapStateToProps = () => (state) => ({
    housingTypes: state.components.forms.event.eventEdit.mainInfo.commonInfo.housingTypes
});

export default connect(mapStateToProps, {getHousingTypes})(CommonInfo)(FORM_ID);