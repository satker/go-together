import React, {useState} from 'react';
import CardMedia from "@material-ui/core/CardMedia";

import {getSrcForImg} from "forms/utils/utils";
import ErrorMessage from "forms/utils/components/LoadableContent/ErrorMessage";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import ImageSelector from "forms/utils/components/ImageSelector";

import {handlePhoto} from "../validation";
import {GOOD_PHOTO} from "../constants";

const PhotoField = ({userPhoto, setUserPhoto, setIsIncorrectData}) => {
    const [checkedPhoto, setCheckedPhoto] = useState(GOOD_PHOTO);

    return <ItemContainer>
        {userPhoto && <CardMedia style={{height: '255px', width: '350px'}}
                                 component="img"
                                 image={getSrcForImg(userPhoto)}/>}
        <ImageSelector photos={userPhoto}
                       setPhotos={photo => {
                           handlePhoto(photo, setCheckedPhoto, setIsIncorrectData);
                           setUserPhoto(photo)
                       }}
                       multiple={false}
        />
        <ErrorMessage error={checkedPhoto}/>
    </ItemContainer>;
}

export default PhotoField;