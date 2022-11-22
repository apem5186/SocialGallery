import { configureStore, createSlice} from '@reduxjs/toolkit';
import { axios } from 'axios';

// Reply Slice
let reply  = createSlice({
    name : 'reply',
    initialState : {replyList:[]},
    reducers:{
        setReply:(state,action)=>{
            state.replyList = action.payload
        }
    },

})

export const {setReply} = reply.actions;
const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"

// postAll Slice
let postAll  = createSlice({
    name : 'postAll',
    initialState : {postAllList:[]},
    reducers:{
        setPostAll:(state,action)=>{
            state.postAllList = action.payload
        }
    },

})

//mainImg payload
// export const fetchMainImg = () => {
//   return (dispatch)=>{
//       axios.get('http://localhost:8080/api/post')
//       .then((res) => {
//         dispatch(setMainImg([...res.data.list]));
//       })
//       .catch((err) => {
//         console.log(err);
//       });
//   }
// };

export const {setPostAll} = postAll.actions;


// Upload Post Title Slice
let postTitle  = createSlice({
    name : 'postTitle',
    initialState : {postTitleList:[]},
    reducers:{
        setPostTitle:(state,action)=>{
            state.postTitleList = action.payload
        }

    },
})
export const {setPostTitle} = postTitle.actions;


// Upload Post content Slice
let postContent  = createSlice({
    name : 'postContent',
    initialState : {postContentList:[]},
    reducers:{
        setPostContent:(state,action)=>{
            state.postContentList = action.payload
        }

    },
})

export const {setPostContent} = postContent.actions;




// Search filter Slice
let searchImg  = createSlice({
    name : 'searchImg',
    initialState : {searchList:[]},
    reducers:{
        setSearchImg:(state,action)=>{
            state.searchList = action.payload
        }
    },
})

export const {setSearchImg} = searchImg.actions;

// isLogin Slice
let isLogin = createSlice({
    name : 'isLogin',
    initialState : {isLoginList :false},
    reducers : {
        setIsLogin : (state,action) =>{
            state.isLoginList = action.payload
        }
    }
})

// isLogin fetch
export const fetchIsLogin = () => {
    return (dispatch)=>{
        axios.get(dev_url + "/findUserByEmail/" + localStorage.getItem("user"))
            .then((res) => {
                dispatch(setIsLogin(res.data.data.isLogin));
            })
            .catch((err) => {
                console.log(err);
            });
    }
};

export const { setIsLogin }= isLogin.actions;

//User Data
let userData  = createSlice({
    name : 'userData',
    initialState : {userDataList:[]},
    reducers:{
        setUserData:(state,action)=>{
            state.userDataList = action.payload
        }
    },
})
export const {setUserData} = userData.actions;

//configureStore
export default configureStore({
    reducer: {
        reply : reply.reducer,
        postAll : postAll.reducer,
        postTitle : postTitle.reducer,
        postContent : postContent.reducer,
        searchImg : searchImg.reducer,
        isLogin : isLogin.reducer,
        userData : userData.reducer,
    },
    // middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(logger),
})

  