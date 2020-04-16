import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import {Button, FormGroup, Input} from "reactstrap";
import {Switch} from "@material-ui/core";
import {createFileReaderToParsePhoto, createPhotoObj} from "../../utils";
import {PhotoObject} from "../../types";

const ImageSelector = ({photos, setPhotos, multiple}) => {
    const [isPhotoUrl, setIsPhotoUrl] = useState(false);
    const [photoFiles, setPhotoFiles] = useState([]);
    const [photosToPush, setPhotosToPush] = useState([]);

    const [fileLength, setFileLength] = useState(0);
    const [currentLength, setCurrentLength] = useState(0);

    const addPhoto = () => {
        isPhotoUrl ? addPhotoUrl(photosToPush) : addPhotoFile(photosToPush.files);
    };

    const addPhotoUrl = (roomPhotoUrl) => {
        setFileLength(1);
        const photo = createPhotoObj(true, roomPhotoUrl);
        setPhotoFiles(photo);
        setCurrentLength(1);
    };

    const addPhotoFile = (customPhotoFiles) => {
        setFileLength(customPhotoFiles.length);
        for (const customPhotoFile of customPhotoFiles) {
            createFileReaderToParsePhoto(customPhotoFile).then(result => {
                photoFiles.push(createPhotoObj(false, result));
                setCurrentLength(photoFiles.length);
            })
        }
    };

    useEffect(() => {
        if (photoFiles.length !== 0 && currentLength === fileLength) {
            setPhotos(multiple ? [...photos, ...photoFiles] : photoFiles[0]);
            setPhotoFiles([])
        }
    }, [currentLength, fileLength, photoFiles, setPhotoFiles, setPhotos, photos, multiple]);

    return <>
        <FormGroup>
            Way to add photo by {isPhotoUrl ? ' URL' : ' file'}<Switch checked={isPhotoUrl}
                                                                       onChange={() => setIsPhotoUrl(!isPhotoUrl)}
                                                                       value='photo'
        />
            {isPhotoUrl ?
                <Input type="text"
                       name="photoUrl"
                       id="photoUrl"
                       placeholder="http://photo.com/photo.jpg"
                       onChange={(evt) => setPhotosToPush(evt.target.value)}/>
                : <Input type="file"
                         multiple={multiple}
                         name="file"
                         onChange={(evt) => setPhotosToPush(evt.target)}/>}
        </FormGroup>
        <Button outline color="success" disabled={!photosToPush} onClick={addPhoto}>Add photo</Button>
    </>;
};

ImageSelector.props = {
    photos: PropTypes.arrayOf(PhotoObject),
    setPhotos: PropTypes.func.isRequired,
    multiple: PropTypes.bool.isRequired
};

export default ImageSelector;