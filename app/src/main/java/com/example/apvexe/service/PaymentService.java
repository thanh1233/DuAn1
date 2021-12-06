//package com.example.apvexe.service;
//
//import com.example.apvexe.model.PaymentInfo;
//
//import retrofit2.Call;
//import retrofit2.http.Headers;
//import retrofit2.http.POST;
//import retrofit2.http.Query;
//
//
//public interface PaymentService {
//
//    @Headers({
//            "Authorization: Basic c2tfdGVzdF81MUszS0YwSm5nNTVZejh1MDJ4aE1nVkFHVDhHUkdUN3pCUDRGb3k0Y2JqRUVaVUk2aTRtc2pYZ3NRdGMzMGtLTXE2NXRpSkpUTWcxSjdJdUZ2enJrWGVUODAwMkNKNU9SSjc6"
//    })
//    @POST("payment_intents")
//    Call<PaymentInfo> CreatePayment(@Query("amount") int amount,
//                                    @Query("currency") String current,
//                                    @Query("payment_method_types[]") String paymentMethod);
//}
