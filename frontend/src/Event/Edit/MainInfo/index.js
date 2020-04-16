import React, {useEffect} from 'react';
import {FormGroup, Input, Label} from "reactstrap";
import {connect} from "../../../Context";
import ImageSelector from "../../../utils/components/ImageSelector";
import {Event} from "../../../types";
import SelectBox from "../../../utils/components/SelectBox";
import PropTypes from "prop-types";
import GroupItems from "../../../utils/components/CardItems";
import CheckInOutDates from "../../../utils/components/CheckInOutDates";
import ContainerColumn from "../../../utils/components/Container/ContainerColumn";
import LeftContainer from "../../../utils/components/Container/LeftContainer";
import RightContainer from "../../../utils/components/Container/RightContainer";
import {FORM_ID} from "../constants";
import {getHousingTypes} from "./actions";

const MainInfo = ({event, onChangeEvent, housingTypes, getHousingTypes}) => {
    useEffect(() => {
        getHousingTypes();
    }, [getHousingTypes]);

    return <ContainerColumn>
        <LeftContainer style={{width: '600px'}}>
            <FormGroup>
                <Label for="name">Event name:</Label>
                <Input type="text"
                       id="name"
                       value={event.name}
                       placeholder='Enter event name'
                       onChange={(evt) => onChangeEvent('name', evt.target.value)}/>
            </FormGroup>
            <SelectBox onChange={(value) => onChangeEvent('housingType', housingTypes
                .filter(type => type.id === value)[0].id)}
                       labelText='Housing type'
                       value={event.housingType}
                       items={housingTypes}/>
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
        </LeftContainer>
        <RightContainer style={{width: '600px'}}>
            <GroupItems items={event.eventPhotoDto.photos}
                        isPhotos
                        onDelete={(id) => console.log('delete: ', id)}/>
            <ImageSelector
                photos={event.eventPhotoDto.photos}
                setPhotos={(photos) => onChangeEvent('eventPhotoDto.photos', photos)}
                multiple={true}
            />
        </RightContainer>
    </ContainerColumn>;
};

MainInfo.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired,
    housingTypes: PropTypes.array.isRequired,
    getHousingTypes: PropTypes.func.isRequired
};

const mapStateToProps = (state) => ({
    housingTypes: state[FORM_ID].housingTypes || []
});

export default connect(mapStateToProps, {getHousingTypes}, FORM_ID)();