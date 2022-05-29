package com.leon.estimate_new.fragments.main_items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.leon.estimate_new.R;
import com.leon.estimate_new.databinding.FragmentSendRequestBinding;
import com.leon.estimate_new.utils.request.SendRequest;

import org.jetbrains.annotations.NotNull;

public class SendRequestFragment extends Fragment {
    private FragmentSendRequestBinding binding;
    private String billId, nationNumber, mobile;
    private boolean isNew = true;

    public SendRequestFragment() {
    }

    public static SendRequestFragment newInstance() {
        return new SendRequestFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSendRequestBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        setOnRadioGroupClickListener();
        setOnButtonClickListener();
    }

    private void setOnButtonClickListener() {
        binding.buttonSendRequest.setOnClickListener(v -> {
            boolean cancel = false;
            billId = binding.editTextBillId.getText().toString();
            nationNumber = binding.editTextNationNumber.getText().toString();
            mobile = binding.editTextMobile.getText().toString();
            if (billId.length() < 6) cancel = cancelForm(binding.editTextBillId);
            if (!cancel && mobile.length() < 11) cancel = cancelForm(binding.editTextMobile);
            if (!cancel && isNew) {
                if ((checkIsNoEmpty(binding.editTextAddress) ||
                        checkIsNoEmpty(binding.editTextFamily) ||
                        checkIsNoEmpty(binding.editTextName))) cancel = true;
                if (!cancel && nationNumber.length() < 10)
                    cancel = cancelForm(binding.editTextNationNumber);
            }
            if (!cancel) {
                sendRequest(isNew, billId, mobile);
            }
        });
    }

    private void sendRequest(boolean isNew, String billId, String mobile) {
        if (isNew)
            new SendRequest(requireContext(), billId, mobile,
                    binding.editTextName.getText().toString(),
                    binding.editTextFamily.getText().toString(),
                    binding.editTextNationNumber.getText().toString(),
                    binding.editTextAddress.getText().toString(), this).execute(requireActivity());
        else
            new SendRequest(requireContext(), billId, mobile, this).execute(requireActivity());
    }

    private boolean cancelForm(final EditText editText) {
        editText.setError(getString(R.string.error_format));
        editText.requestFocus();
        return true;
    }

    private void setOnRadioGroupClickListener() {
        binding.radioGroupRequestType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_button_new) {
                isNew = true;
                binding.linearLayout3.setVisibility(View.VISIBLE);
                binding.linearLayoutNation.setVisibility(View.VISIBLE);
            } else {
                isNew = false;
                binding.linearLayout3.setVisibility(View.GONE);
                binding.linearLayoutNation.setVisibility(View.GONE);
            }
        });
    }

    private boolean checkIsNoEmpty(EditText editText) {
        if (editText.getText().toString().length() < 1) {
            editText.setError(getString(R.string.error_empty));
            editText.requestFocus();
            return true;
        }
        return false;
    }

    public Button getButton() {
        return binding.buttonSendRequest;
    }

    public void afterRequest() {
        requireActivity().runOnUiThread(() -> {
            binding.editTextName.setText("");
            binding.editTextFamily.setText("");
            binding.editTextMobile.setText("");
            binding.editTextBillId.setText("");
            binding.editTextAddress.setText("");
            binding.editTextNationNumber.setText("");
        });
    }
}