import { configureStore, createSlice} from '@reduxjs/toolkit';
import axios from 'axios';

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
const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"

// Reply payload
export const fetchReply = () => {
    return async(dispatch)=>{
        axios.get(dev_url + "/api/comment/all")
        .then((res) => {
          dispatch(setReply([...res.data.list]));
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

export const {setReply} = reply.actions;

// mainImg Slice
let mainImg  = createSlice({
  name : 'mainImg',
  initialState : {mainList:[]},
  reducers:{
      setMainImg:(state,action)=>{
          state.mainList = action.payload
      }
  },

})

//mainImg payload
// export const fetchMainImg = () => {
//   return async(dispatch)=>{
//       axios.get(dev_url + '/api/post')
//       .then((res) => {
//         dispatch(setMainImg([...res.data.list]));
//       })
//       .catch((err) => {
//         console.log(err);
//       });
//   }
// };
export const {setMainImg} = mainImg.actions;


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

// // Search filter payload
// export const fetchSearchImg = () => {
  
//   return async(dispatch)=>{
//       axios.get('localhost:8080/api/post?keyword=' + {searchTitle})
//       .then((res) => {
//         dispatch(setMainImg([...res.data.list]));
//       })
//       .catch((err) => {
//         console.log(err);
//       });
//   }
// };
// export const {setSearchImg} = searchImg.actions;
  



//configureStore
export default configureStore({
    reducer: {
        reply : reply.reducer,
        mainImg : mainImg.reducer,
        postTitle : postTitle.reducer,
        postContent : postContent.reducer,
        searchImg : searchImg.reducer,
    },
    // middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(logger),
  })

  