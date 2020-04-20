import {FormGroup, Input, Label} from "reactstrap";
import SelectBox from "../../../../utils/components/SelectBox";
import CheckInOutDates from "../../../../utils/components/CheckInOutDates";
import React, {useEffect} from "react";
import {FORM_ID} from "../../constants";
import {getHousingTypes} from "../actions";
import {connect} from "../../../../../App/Context";
import {Event, ResponseData} from "../../../../utils/types";
import PropTypes from "prop-types";

const CommonInfo = ({event, onChangeEvent, getHousingTypes, housingTypes}) => {
    useEffect(() => {
        getHousingTypes();
    }, [getHousingTypes]);

    return <>
        <FormGroup>
            <Label for="name">Event name:</Label>
            <Input type="text"
                   id="name"
                   value={event.name}
                   placeholder='Enter event name'
                   onChange={(evt) => onChangeEvent('name', evt.target.value)}/>
        </FormGroup>
        <SelectBox onChange={(value) => onChangeEvent('housingType', housingTypes.response
            .filter(type => type.id === value)[0].id)}
                   labelText='Housing type'
                   value={event.housingType}
                   items={housingTypes.response}/>
        <FormGroup>
            <Label for="description">Description</Label>
            <Input type="textarea"
                   name="text"
                   id="description"
                   value={event.description}
                   placeholder="Home, dear home..."
                   onChange={(evt) => onChangeEvent('description', evt.target.value)}/>
        </FormGroup>
        <FormGroup>
            <Label for="peopleCount">People count: </Label>
            <Input type="number"
                   name="text"
                   id="peopleCount"
                   value={event.peopleCount}
                   onChange={(evt) => onChangeEvent('peopleCount', evt.target.value)}/>
        </FormGroup>
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