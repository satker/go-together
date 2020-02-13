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

const MainInfo = ({event, onChange}) => {
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
                       value={event.apartmentName}
                       placeholder='Enter event name'
                       onChange={(evt) => onChange(evt.target.value, 'name')}/>
            </FormGroup>
            <SelectBox onChange={(value) => onChange(housingTypes
                .filter(type => type.id === value)[0].id, 'housingType')}
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
                       onChange={(evt) => onChange(evt.target.value, 'description')}/>
            </FormGroup>
            Trip dates: <CheckInOutDates startDate={event.startDate}
                                         endDate={event.endDate}
                                         setStartDate={startDate => onChange(startDate, 'startDate')}
                                         setEndDate={endDate => onChange(endDate, 'endDate')}
        />
        </div>
        <div className='container-main-info-item center-items fixed-width-content'>
            <GroupItems items={event.eventPhotoDto.photos}
                        isPhotos
                        onDelete={(id) => console.log('delete: ', id)}/>
            <ImageSelector
                photos={event.eventPhotoDto.photos}
                setPhotos={(photos) => {
                    onChange(photos, 'eventPhotoDto.photos')
                }}
                multiple={true}
            />
        </div>
    </div>;
};

MainInfo.propTypes = {
    event: Event.isRequired,
    onChange: PropTypes.func.isRequired
};

export default MainInfo;