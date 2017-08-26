package businessObjects.connect;

import businessObjects.domain.Bill;
import businessObjects.domain.Category;
import businessObjects.domain.Product;
import businessObjects.dto.CustomerDTO;

import java.util.List;

import businessObjects.domain.CustomerRegistration;
import businessObjects.domain.CustomersLogin;
import businessObjects.domain.EmployeeDetails;

import businessObjects.dto.AdminDTO;
import businessObjects.dto.CustomerListDTO;
import businessObjects.dto.CustomerLoginDTO;
import businessObjects.dto.ProdcastDTO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sarathan732 on 8/5/2017.
 */

public interface ProdcastService {
    @POST("prodcast/customer/login")
    @FormUrlEncoded
    Call<CustomerLoginDTO<CustomersLogin>> login(@Field("userid") String userId,
                                                 @Field("password") String password,
                                                 @Field("country") String country);

    @POST("prodcast/customer/retrievePin")
    @FormUrlEncoded
    Call<AdminDTO> retrieve(@Field("mobilePhone") String mobilePhone,
                            @Field("country") String country);

    @POST("prodcast/customer/changePinNumber")
    @FormUrlEncoded
    Call<ProdcastDTO> changePinNumber(@Field("accessId") long accessId,
                                      @Field("oldPinNumber") String oldPinNumber,
                                      @Field("newPinNumber") String newPinNumber);

    @GET("prodcast/customer/getDistributorList")
     Call<CustomerLoginDTO> getAllDistributors(@Query("accessId") long accessId);

    @GET("prodcast/global/customer")
    Call<CustomerDTO> getBills(@Query("id") long customerId, @Query("employeeId") long employeeId);

     @POST("prodcast/customer/getCustomerDetails")
     @FormUrlEncoded
     Call<AdminDTO<EmployeeDetails>> getCustomerDetails(@Field("accessId") long accessId,
                                                        @Field("distributorId") long distributorId);

    @POST("prodcast/customer/customerRegistration")
    @FormUrlEncoded
    Call<CustomerLoginDTO<CustomerRegistration>> register(@Field("country") String country,
                                                          @Field("mobilePhone") String mobilePhone,
                                                          @Field("pinNumber") String pinNumber);

    @POST("prodcast/customer/confirmationDetails")
    @FormUrlEncoded
    Call<CustomerLoginDTO<CustomersLogin>> verify(@Field("accessId") long country,
                                                  @Field("confirmationCode") String confirmationCode);

    @POST("prodcast/customer/resendConfirmationCode")
    @FormUrlEncoded
    Call<AdminDTO> resendConfirmationCode(@Field("accessId") long accessId);


    @POST("prodcast/customer/saveNewCustomer")
    @FormUrlEncoded
    Call<CustomerListDTO> saveNewCustomer(@Field("customerId") String customerId,
                                          @Field("firstName") String firstName,
                                          @Field("lastName") String lastName,
                                          @Field("emailAddress") String emailAddress,
                                          @Field("cellPhone") String cellPhoneNumber,
                                          @Field("homePhoneNumber") String homePhoneNumber,
                                          @Field("billingAddress1") String billingAddress1,
                                          @Field("billingAddress2") String billingAddress2,
                                          @Field("billingAddress3") String billingAddress3,
                                          @Field("city") String city,
                                          @Field("state") String state,
                                          @Field("country") String country,
                                          @Field("postalCode") String postalCode,
                                          @Field("smsAllowed") boolean smsAllowed);


    @GET("prodcast/distributor/getCategory")
    Call<AdminDTO<List<Category>>> getCategory(@Query("employeeId") long employeeId);


    @GET("prodcast/distributor/getProducts")
    Call<AdminDTO<List<Product>>> getProducts(@Query("employeeId") long employeeId);



}