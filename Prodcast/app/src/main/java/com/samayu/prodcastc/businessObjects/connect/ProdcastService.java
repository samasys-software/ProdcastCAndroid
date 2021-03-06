package com.samayu.prodcastc.businessObjects.connect;

import com.samayu.prodcastc.businessObjects.domain.Category;
import com.samayu.prodcastc.businessObjects.domain.CustomerRegistration;
import com.samayu.prodcastc.businessObjects.domain.CustomersLogin;
import com.samayu.prodcastc.businessObjects.domain.EmployeeDetails;
import com.samayu.prodcastc.businessObjects.domain.NewCustomerRegistrationDetails;
import com.samayu.prodcastc.businessObjects.domain.Product;
import com.samayu.prodcastc.businessObjects.dto.AdminDTO;
import com.samayu.prodcastc.businessObjects.dto.CountryDTO;
import com.samayu.prodcastc.businessObjects.dto.CustomerDTO;
import com.samayu.prodcastc.businessObjects.dto.CustomerListDTO;
import com.samayu.prodcastc.businessObjects.dto.CustomerLoginDTO;
import com.samayu.prodcastc.businessObjects.dto.CustomerReportDTO;
import com.samayu.prodcastc.businessObjects.dto.OrderDTO;
import com.samayu.prodcastc.businessObjects.dto.OrderDetailDTO;
import com.samayu.prodcastc.businessObjects.dto.ProdcastDTO;
import com.samayu.prodcastc.businessObjects.dto.ProductListDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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

    @POST("prodcast/support/newIssue")
    @FormUrlEncoded
    Call<ProdcastDTO> raiseRequest(@Field("phoneNumber") String phoneNumber,
                                         @Field("issue") String issue,
                                         @Field("countryId") String countryId);

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

    @POST("prodcast/global/saveOrder")
    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    Call<CustomerDTO> saveOrder(@Body OrderDetailDTO body);


    @GET("prodcast/distributor/getCategory")
    Call<AdminDTO<List<Category>>> getCategory(@Query("employeeId") long employeeId);


    @GET("prodcast/distributor/getProducts")
    Call<ProductListDTO> getProducts(@Query("employeeId") long employeeId);



    @GET("prodcast/global/billdetails")
    Call<OrderDTO> getBillDetails(@Query("billId") long id,
                                  @Query("employeeId") long employeeId,
                                  @Query("userRole") String userRole) ;


    @GET("prodcast/customer/reportForCustomers")
    Call<CustomerReportDTO> reportForCustomers(@Query("reportType") String reportType,
                                        @Query("accessId") long accessId,
                                        @Query("startDate") String customStartDate,
                                        @Query("endDate") String customEndDate,
                                        @Query("selectedDistributor") long selectedDistributor,
                                        @Query("reportId") String reportId);


    @GET("prodcast/customer/getNewCustomerRegistrationDetails")
    Call<CustomerListDTO<NewCustomerRegistrationDetails>>  getNewCustomerRegistrationDetails(@Query("accessId") long accessId);

    @GET("prodcast/global/getCountries")
    Call<CountryDTO> getCountries();



}