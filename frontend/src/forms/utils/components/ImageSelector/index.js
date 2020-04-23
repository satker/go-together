import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import {Switch} from "@material-ui/core";
import {createFileReaderToParsePhoto, createPhotoObj} from "../../utils";
import {PhotoObject} from "../../types";
import Button from "@material-ui/core/Button";
import LabeledInput from "../../LabeledInput";

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
        <>
            Way to add photo by {isPhotoUrl ? ' URL' : ' file'}<Switch checked={isPhotoUrl}
                                                                       onChange={() => setIsPhotoUrl(!isPhotoUrl)}
                                                                       value='photo'
        />
            {isPhotoUrl ?
                <LabeledInput
                    id="photoUrl"
                    label="Photo url"
                    onChange={setPhotosToPush}
                />
                : <input type="file"
                         multiple={multiple}
                         name="file"
                         onChange={(evt) => setPhotosToPush(evt.target)}/>}
        </>
        <Button outline color="success" disabled={!photosToPush} onClick={addPhoto}>Add photo</Button>
    </>;
};

ImageSelector.props = {
    photos: PropTypes.arrayOf(PhotoObject),
    setPhotos: PropTypes.func.isRequired,
    multiple: PropTypes.bool.isRequired
};

export default ImageSelector;