import React, {useContext, useEffect, useState} from 'react';
import {FormGroup, Input, Label} from "reactstrap";
import {Context} from "../../../Context";
import {EVENT_SERVICE_URL} from "../../../utils/constants";
import ImageSelector from "../../../utils/components/ImageSelector";
import {Event} from "../../../types";
import SelectBox from "../../../utils/components/SelectBox";
import PropTypes from "prop-types";
import GroupItems from "../../../utils/components/CardItems";
import CheckInOutDates from "../../../utils/components/CheckInOutDates";

const MainInfo = ({event, onChangeEvent}) => {
    const [housingTypes, setHousingTypes] = useState([]);
    const [state] = useContext(Context);

    useEffect(() => {
        state.fetchWithToken(EVENT_SERVICE_URL + '/events/housingTypes', setHousingTypes);
    }, [state]);

    return <div className='container-main-info'>
        <div className='container-main-info-item fixed-width-content'>
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
        </div>
        <div className='container-main-info-item center-items fixed-width-content'>
            <GroupItems items={event.eventPhotoDto.photos}
                        isPhotos
                        onDelete={(id) => console.log('delete: ', id)}/>
            <ImageSelector
                photos={event.eventPhotoDto.photos}
                setPhotos={(photos) => onChangeEvent('eventPhotoDto.photos', photos)}
                multiple={true}
            />
        </div>
    </div>;
};

MainInfo.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired
};

export default MainInfo;