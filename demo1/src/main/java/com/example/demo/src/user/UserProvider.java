package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.GetRes.*;
import com.example.demo.src.user.model.PostReq.PostLoginReq;
import com.example.demo.src.user.model.PostRes.PostLoginRes;
import com.example.demo.src.user.model.User.User;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
            // [Business Layer]는 컨트롤러와 데이터 베이스를 연결
/**
 * Provider란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Read의 비즈니스 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
public class UserProvider {


    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final UserDao userDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }
    // ******************************************************************************


    // 로그인(password 검사)
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {

        //
        User user2 = userDao.getStatus(postLoginReq);
        if(user2.getStatus().equals("inactive"))
        {
            throw new BaseException(POST_USERS_INACTIVE_STATUS);
        }
        try{
            User user = userDao.getPwd(postLoginReq);
        }catch (Exception ignored){
            throw new BaseException(EMPTY_EMAIL);
        }

        User user = userDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword()); // 암호화
            // 회원가입할 때 비밀번호가 암호화되어 저장되었기 떄문에 로그인을 할때도 암호화된 값끼리 비교를 해야합니다.
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (postLoginReq.getPassword().equals(password)) { //비말번호가 일치한다면 userIdx를 가져온다.
            int userIdx = userDao.getPwd(postLoginReq).getUserIdx();
            //return new PostLoginRes(userIdx);
//  *********** 해당 부분은 7주차 - JWT 수업 후 주석해제 및 대체해주세요!  **************** //
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
//  **************************************************************************

        } else { // 비밀번호가 다르다면 에러메세지를 출력한다.
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    // 해당 이메일이 이미 User Table에 존재하는지 확인
    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // User들의 정보를 조회
    public List<GetUserRes> getUsers() throws BaseException {
        try {
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // User의 주문내역을 조회
    public List<GetOrderRes> getOrders(int page) throws BaseException {
        try {
            List<GetOrderRes> getUserRes = userDao.getOrders(page);
            return getUserRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // 해당 nickname을 갖는 User들의 정보 조회
    public List<GetUserRes> getUsersByNickname(String nickname) throws BaseException {
        try {
            List<GetUserRes> getUsersRes = userDao.getUsersByNickname(nickname);
            return getUsersRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 userIdx를 갖는 User의 정보 조회
    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 userIdx를 갖는 user의 쿠폰 정보 조회
    public GetCouponRes getCoupon(int userIdx) throws BaseException {
        try {
            GetCouponRes getUserRes = userDao.getCoupon(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 userIdx를 갖는 user의 포인트 정보 조회
    public GetPointRes getPoint(int userIdx) throws BaseException {
        try {
            GetPointRes getUserRes = userDao.getPoint(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 basketIdx를 갖는 user의 장바구니 정보 조회
    public GetBasketRes getBasket(int basketIdx) throws BaseException {
        try {
            GetBasketRes getUserRes = userDao.getBasket(basketIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 userIdx를 갖는 store의 포인트 정보 조회
    public GetStoreRes getStore(int storeIdx) throws BaseException {
        try {
            GetStoreRes getUserRes = userDao.getStore(storeIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // User들의 포인트 정보를 조회
    public List<GetPointRes> getPoints() throws BaseException {
        try {
            List<GetPointRes> getUserRes = userDao.getPoints();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 가게 카테고리 조회
    public List<GetCategoryRes> getCategory() throws BaseException {
        try {
            List<GetCategoryRes> getUserRes = userDao.getCategory();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 가게들의 정보를 조회
    public List<GetStoreRes> getStores() throws BaseException {
        try {

            List<GetStoreRes> getUserRes = userDao.getStore();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 해당 nickname을 갖는 User들의 정보 조회
    public List<GetPointRes> getPointsByNickname(String usePlace) throws BaseException {
        try {
            List<GetPointRes> getUsersRes = userDao.getPointsByNickname(usePlace);
            return getUsersRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
