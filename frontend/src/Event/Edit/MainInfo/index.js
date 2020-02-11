import React, {useContext, useEffect, useState} from 'react';
import {FormGroup, Input, Label} from "reactstrap";
import {Context} from "../../../Context";
import {EVENT_SERVICE_URL} from "../../../utils/constants";
import {createFileReaderToParsePhoto, createPhotoObj, getSrcForImg} from "../../../utils/utils";
import ImageSelector from "../../../utils/components/ImageSelector";
import {CreateApartment} from "../../../types";
import SelectBox from "../../../utils/components/SelectBox";
import MultipleSelectBox from "../../../utils/components/MultipleSelectBox";

const MainInfo = ({apartment, onChange}) => {
    const [apartmentTypes, setApartmentTypes] = useState([]);
    const [isUrlPhoto, setIsUrlPhoto] = useState(null);
    const [parameters, setParameters] = useState([]);
    const [apartmentPhoto, setApartmentPhoto] = useState(null);

    const [apartmentParameters, setApartmentParameters] = useState([]);

    const [state] = useContext(Context);

    useEffect(() => {
        if (apartment) {
            if (!apartment.parameters.every(param => apartmentParameters.map(p => p.value).includes(param.id))) {
                setApartmentParameters(apartment.parameters.map(param => ({value: param.id, label: param.name})));
            }
            if (!apartmentPhoto && (apartment.mainPhoto.photoUrl || apartment.mainPhoto.content.type)) {
                setApartmentPhoto(getSrcForImg(apartment.mainPhoto))
            }
        }
    }, [apartment, apartmentParameters, apartmentPhoto]);

    useEffect(() => {
        state.fetchWithToken(EVENT_SERVICE_URL + '/parameters', setParameters);
        state.fetchWithToken(EVENT_SERVICE_URL + '/types', setApartmentTypes);
    }, [state]);

    const onPhotoUrlChangeHandler = (e) => {
        const photoUrl = e.target.value;
        onChange(photoUrl, 'mainPhoto.photoUrl');
        setApartmentPhoto(photoUrl);

    };

    const onFileChangeHandler = (e) => {
        const file = e.target.files.item(0);
        createFileReaderToParsePhoto(file, (parsedPhoto) => {
            setApartmentPhoto(parsedPhoto);
            onChange(createPhotoObj(false, parsedPhoto), 'mainPhoto')
        });
    };

    return <div className='container-main-info'>
        <div className='container-main-info-item fixed-width-content'>
            <FormGroup>
                <Label for="apartmentName">Apartment name:</Label>
                <Input type="text"
                       id="apartmentName"
                       value={apartment.apartmentName}
                       placeholder='Enter apartment name'
                       onChange={(evt) => onChange(evt.target.value, 'apartmentName')}/>
            </FormGroup>
            <SelectBox onChange={(evt) => onChange(apartmentTypes
                .filter(type => type.id === evt.target.value)[0], 'apartmentType')}
                       labelText='Apartment type'
                       value={apartment.apartmentType && apartment.apartmentType.id}
                       items={apartmentTypes}/>
            <MultipleSelectBox label='Apartment parameters:'
                               value={apartmentParameters}
                               optionsSimple={parameters}
                               onChange={evt => {
                                   onChange(evt.map(param => ({id: param.value, name: param.label})), 'parameters');
                                   setApartmentParameters(evt);
                               }}/>
            <FormGroup>
                <Label for="description">Description</Label>
                <Input type="textarea"
                       name="text"
                       id="description"
                       value={apartment.description}
                       placeholder="Home, dear home..."
                       onChange={(evt) => onChange(evt.target.value, 'description')}/>
            </FormGroup>
        </div>
        <div className='container-main-info-item center-items fixed-width-content'>
            {apartmentPhoto && <img src={apartmentPhoto} width='400' height='300' alt='150'/>}
            <ImageSelector isUrlPhoto={isUrlPhoto}
                           onFileChangeHandler={onFileChangeHandler}
                           onPhotoUrlChangeHandler={onPhotoUrlChangeHandler}
                           setIsUrlPhoto={setIsUrlPhoto}
                           multiple={false}
            />
        </div>
    </div>;
};

MainInfo.propTypes = {
    apartment: CreateApartment.isRequired
};

export default MainInfo;