import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import {Switch} from "@material-ui/core";

import {createFileReaderToParsePhoto, createPhotoObj} from "forms/utils/utils";
import {PhotoObject} from "forms/utils/types";
import LabeledInput from "forms/utils/components/LabeledInput";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import ContainerRow from "forms/utils/components/Container/ContainerRow";

const ImageSelector = ({photos, setPhotos, multiple}) => {
    const [isPhotoUrl, setIsPhotoUrl] = useState(false);
    const [photoFiles, setPhotoFiles] = useState([]);
    const [fileLength, setFileLength] = useState(0);
    const [lock, setLock] = useState(false);

    const addPhotoUrl = (roomPhotoUrl) => {
        setFileLength(1);
        const photo = createPhotoObj(true, roomPhotoUrl);
        setPhotoFiles(photo);
    };

    const addPhotoFile = (customPhotoFiles) => {
        setFileLength(customPhotoFiles.length);
        for (const customPhotoFile of customPhotoFiles) {
            createFileReaderToParsePhoto(customPhotoFile).then(result => {
                photoFiles.push(createPhotoObj(false, result));
                setLock(true);
            })
        }
    };

    useEffect(() => {
        if (lock) {
            setLock(false);
        }
        if (photoFiles.length !== 0 && photoFiles.length === fileLength) {
            setPhotos(multiple ? [...photos, ...photoFiles] : photoFiles[0]);
            setPhotoFiles([])
        }
    }, [fileLength, photoFiles, setPhotos, photos, multiple, lock]);

    return <ContainerRow>
        <ItemContainer>
            Way to add photo by {isPhotoUrl ? ' URL' : ' file'}<Switch checked={isPhotoUrl}
                                                                       onChange={() => setIsPhotoUrl(!isPhotoUrl)}
                                                                       value='photo'
        />
        </ItemContainer>
        <ItemContainer>
            {isPhotoUrl ?
                <LabeledInput
                    id="photoUrl"
                    label="Photo url"
                    onChange={addPhotoUrl}
                />
                : <input type="file"
                         multiple={multiple}
                         name="file"
                         onChange={(evt) => addPhotoFile(evt.target.files)}/>}
        </ItemContainer>
    </ContainerRow>;
};

ImageSelector.propTypes = {
    photos: PropTypes.arrayOf(PhotoObject),
    setPhotos: PropTypes.func.isRequired,
    multiple: PropTypes.bool.isRequired
};

export default ImageSelector;